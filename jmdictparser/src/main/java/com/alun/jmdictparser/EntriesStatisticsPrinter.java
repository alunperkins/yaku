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
package com.alun.jmdictparser;

import com.alun.common.models.DictEntry;
import com.alun.common.models.Gloss;
import com.alun.common.models.Kanji;
import com.alun.common.models.Lang;
import com.alun.common.models.Sense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntriesStatisticsPrinter {
    void printStatistics(List<DictEntry> entries) {
        System.out.println("Number of entries: " + entries.size());

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
                            .filter(sense ->
                                    sense.getGlosses().stream().anyMatch(gloss -> gloss.getLang().equals(Lang.ENG))
                            );
                    return Collections.singletonList(englishSenses.count());
                }
        );

        System.out.println("======= (NON-EMPTY) ENGLISH GLOSSES PER SENSE =======");
        histogram(entries, entry -> entry.getSenses()
                .stream()
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
                .mapToLong(sense -> sense.getGlosses().stream().filter(gloss -> gloss.getLang() == Lang.ENG).count())
                .sum()
        ));

        System.out.println("======= IS THERE ANY SENSE THAT HAS GLOSSES OF MULTIPLE LANGUAGES? If not it would make more sense to put language at the Sense level =======");
        for (DictEntry dictEntry : entries) {
            for (Sense sense : dictEntry.getSenses()) {
                final List<Gloss> glosses = sense.getGlosses();
                if (glosses.size() == 0) {
                    System.out.println("Size zero gloss - should never happen!"); // put breakpoint here
                    continue;
                }
                final Lang lang = glosses.get(0).getLang();
                if (glosses.stream().anyMatch(gloss -> gloss.getLang() != lang)) {
                    System.out.println("Heterogeneous language sense found"); // put breakpoint here
                }
            }
        }
        // No, in the copy of JMDict I have there is no word with a sense having glosses of different languages, so yes it would make more sense to put language at the sense level

        System.out.println("======= TRANSLATED JAPANESE WORDS PER LANGUAGE - would probably have to be above 40,000 or so to be useful as a dictionary for that language =======");
        histogram(entries, entry -> new ArrayList<>(
                        entry.getSenses()
                                .stream()
                                .map(sense -> sense.getGlosses().get(0).getLang()) // the language of each sense (assuming glosses of a single sense are homogeneous in lang)
                                .collect(Collectors.toSet()) // remove duplicates to get a list of languages supported by this entry
                )
        );
    }

    void analyzeHowReRestrIsUsed(List<DictEntry> entries) {
        System.out.println("======= re_restr use =======");
        AtomicInteger noOfEntriesWithDisjointReRestrCombinations = new AtomicInteger(0);
        AtomicInteger noOfEntriesWithPartiallyOverlappingReRestrCombinations = new AtomicInteger(0);
        for (DictEntry entry : entries) {
            boolean disjoint = true;

            final List<HashSet<String>> reRestrCombinations = entry.getKanas().stream()
                    .filter(kana -> kana.getOnlyForKanjis() != null)
                    .map(kana -> new HashSet<>(kana.getOnlyForKanjis()))
                    .collect(Collectors.toList());

            if (reRestrCombinations.isEmpty()) continue;

            outerLoop:
            for (int i = 0; i < reRestrCombinations.size() - 1; i++) {
                final HashSet<String> a = reRestrCombinations.get(i);
                for (int j = i + 1; j < reRestrCombinations.size(); j++) {
                    final HashSet<String> b = reRestrCombinations.get(j);
                    if (!a.equals(b) && a.stream().anyMatch(b::contains)) {
                        disjoint = false;
                        System.out.println("Warning: entry " + entry.getId() + " has kanas with re_restr tags that overlap partially but not completely");
                        break outerLoop;
                    }
                }
            }
            if (disjoint) noOfEntriesWithDisjointReRestrCombinations.getAndIncrement();
            else noOfEntriesWithPartiallyOverlappingReRestrCombinations.getAndIncrement();
        }
        System.out.println("There are " + noOfEntriesWithDisjointReRestrCombinations + " entries with re_restr combinations that are disjoint, " +
                "and " + noOfEntriesWithPartiallyOverlappingReRestrCombinations + " entries with re_restr combinations that are partially overlapping");
    }

    private <T extends Comparable<T>> void histogram(List<DictEntry> entries, final Function<DictEntry, List<T>> getKeys) {
        final TreeMap<T, Long> counts = new TreeMap<>();
        for (DictEntry entry : entries) {
            final List<T> keys = getKeys.apply(entry);
            for (T key : keys) {
                if (!counts.containsKey(key)) {
                    counts.put(key, 0L);
                }
//                if ((key instanceof Long) && ((Long) key) > 11) {
//                    System.out.println(entry.toString());
//                }
                counts.put(key, counts.get(key) + 1);
            }
        }
        final long total = counts.values().stream().reduce(0L, Long::sum);
        for (T key : counts.keySet()) {
            long occurrences = counts.get(key);
            System.out.println(key + "," + occurrences + "," + Math.round(100 * (double) occurrences / (double) total) + "%");
        }
    }
}
