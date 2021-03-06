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

import com.alun.jmdictparser.models.DictEntryRaw;

import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        final String pathJmDictXml = Objects.requireNonNull(args[0]);
        System.out.println("pathJmDictXml = " + pathJmDictXml);

        final List<DictEntryRaw> entries = new JMDictParser().run(pathJmDictXml);

        final EntriesValidator validator = new EntriesValidator();
        validator.checkSampleEntry(entries);
        validator.validateThatAllNonNullMembersAreNonEmpty(entries);
        validator.checkInternalReferentialIntegrityOfEachEntry(entries);

        final EntriesStatisticsPrinter analyzer = new EntriesStatisticsPrinter();
        analyzer.printVariousStatistics(entries);
        analyzer.checkPosAvailabilityByLanguage(entries);
        analyzer.analyzeHowReRestrIsUsed(entries);
        analyzer.checkAntonymReferences(entries); // (SLOW)
        analyzer.checkCrossReferences(entries); // (SLOW)
    }
}