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
package com.alun.indexcreator

import com.alun.common.models.*
import javax.xml.parsers.SAXParserFactory

class JMDictParser {
    fun run(path: String): List<DictEntry> {
        val handler = JMDictHandler()
        SAXParserFactory.newInstance().newSAXParser().parse(path, handler)

        checkSample(handler.entries)

        return handler.entries
    }

    private fun checkSample(entries: List<DictEntry>) {
        val entryCheckExpected = DictEntry(
            1038660,
            listOf(Kanji("空オケ", null, null)),
            listOf(
                Kana("からオケ", null, null, null, null),
                Kana("カラオケ", null, listOf(Priority.ICHI1, Priority.SPEC1), null, true)
            ),
            listOf(
                Sense(
                    null,
                    null,
                    listOf(POS.N),
                    null,
                    null,
                    null,
                    listOf(Misc.UK),
                    listOf("from 空 and オーケストラ"),
                    null,
                    null,
                    listOf(Gloss("karaoke", Lang.ENG, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke", Lang.DUT, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoké", Lang.FRE, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("Karaoke (wörtl. leeres Orchester)", Lang.GER, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("караоке", Lang.RUS, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke (petje s posneto spremljavo)", Lang.SLV, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke (cantar música grabada)", Lang.SPA, null), Gloss("karaoke", Lang.SPA, null))
                ), Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke", Lang.SWE, null))
                )

            )
        )
        val entryCheckActual = entries[3445]
        if (entryCheckActual != entryCheckExpected) error("possible parsing error")
    }
}