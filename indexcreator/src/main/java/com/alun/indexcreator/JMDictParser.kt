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