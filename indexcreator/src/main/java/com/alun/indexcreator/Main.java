/*
    Yaku offline browser of Japanese dictionaries
    Copyright (C) 2020 Alun Perkins

    This file is part of Yaku.

    Yaku is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Yaku is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Yaku.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.alun.indexcreator;

import com.alun.common.models.DictEntry;
import com.alun.common.models.Gloss;
import com.alun.common.models.Kanji;
import com.alun.common.models.Lang;
import com.alun.common.models.Sense;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    /**
     * designed to work with my copy of JMdict
     * retrieved Sat 18 Jul 10:20:12 BST 2020
     * Rev 1.09
     * md5sum 31f5a9b3bf2923619a1900bf88719ef5  JMdict.gz
     * sha256sum c72c29e3320a2dbed157eb2c6251b0a3e3a8fefff9f8ff9d4876808eff505165  JMdict.gz
     *
     * @param args path to JMdict.xml
     */
    public static void main(String[] args) {
        final String pathJmDictXml = args[0];
        System.out.println("pathJmDictXml = " + pathJmDictXml);
        final String pathIndexDir = args[1];
        System.out.println("pathIndexDir = " + pathIndexDir);

        new DTDValidator().throwIfDtdInvalid(pathJmDictXml);
        final List<DictEntry> entries = new JMDictParser().run(pathJmDictXml);

        new IndexCreator().run(entries, pathIndexDir);

        analyze(entries);

        System.out.println("Done");
    }

    private static void analyze(List<DictEntry> entries) {
        System.out.println("======= KANJIS PER ENTRY =======");
        histogram(entries, entry -> {
            final List<Kanji> kanjis = entry.getKanjis();
            return Collections.singletonList(Integer.toUnsignedLong(kanjis == null ? 0 : kanjis.size()));
        });

        System.out.println("======= KANAS PER ENTRY =======");
        histogram(entries, entry -> Collections.singletonList(Integer.toUnsignedLong(entry.getKanas().size())));

        System.out.println("======= ENGLISH SENSES PER ENTRY =======");
        histogram(entries, entry -> {
                    Stream<Sense> englishSenses = entry.getSenses().stream()
                            .filter(sense -> sense.getGlosses() != null)
                            .filter(sense ->
                                    sense.getGlosses().stream().anyMatch(gloss -> gloss.getLang().equals(Lang.ENG))
                            );
                    return Collections.singletonList(englishSenses.count());
                }
        );

        System.out.println("======= (NON-EMPTY) ENGLISH GLOSSES PER SENSE =======");
        histogram(entries, entry -> entry.getSenses()
                .stream()
                .filter(sense -> sense.getGlosses() != null)
                .map(sense -> sense.getGlosses()
                        .stream()
                        .filter(gloss -> gloss.getLang() == Lang.ENG)
                        .filter(gloss -> !gloss.getStr().isEmpty())
                        .count()
                ).collect(Collectors.toList())
        );

        System.out.println("======= TOTAL ENGLISH GLOSSES PER ENTRY =======");
        histogram(entries, entry -> Collections.singletonList(entry.getSenses()
                .stream()
                .filter(stream -> stream.getGlosses() != null)
                .mapToLong(sense -> sense.getGlosses().stream().filter(gloss -> gloss.getLang() == Lang.ENG).count())
                .sum()
        ));

    }

    private static void histogram(List<DictEntry> entries, final Function<DictEntry, List<Long>> getKeys) {
        final TreeMap<Long, Long> counts = new TreeMap<>();
        entries.forEach(entry -> {
            final List<Long> keys = getKeys.apply(entry);
            keys.forEach(key -> {
                if (!counts.containsKey(key)) {
                    counts.put(key, 0L);
                }
//                if (key > 19) {
//                    System.out.println(entry.toString());
//                }
                counts.put(key, counts.get(key) + 1);
            });
        });
        final long total = counts.values().stream().reduce(0L, Long::sum);
        counts.keySet().forEach(key -> {
            long occurrences = counts.get(key);
            System.out.println(key + "," + occurrences + "," + Math.round(100 * (double) occurrences / (double) total) + "%");
        });
    }
}