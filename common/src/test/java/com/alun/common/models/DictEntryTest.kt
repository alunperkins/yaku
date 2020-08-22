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
package com.alun.common.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DictEntryTest {
    @Test
    fun shouldSerializeAndDeserialize() {
        val original = DictEntry(
            1,
            listOf(
                Kanji(
                    "kanji1",
                    listOf(KanjiInfo.ATEJI),
                    listOf(Priority.GAI1)
                ),
                Kanji(
                    "kanji2",
                    listOf(
                        KanjiInfo.ATEJI,
                        KanjiInfo.IRREGULAR_KANA
                    ),
                    listOf(Priority.NF02)
                )
            ),
            listOf(
                Kana(
                    "kana",
                    listOf(ReadingInfo.IRREGULAR_KANA),
                    listOf(Priority.ICHI1),
                    listOf("only for kanji1"),
                    true
                )
            ),
            listOf(
                Sense(
                    listOf("k1", "k2"),
                    listOf("k3", "k4"),
                    listOf(POS.ADJ_F, POS.ADJ_NA),
                    listOf("x1", "x2"),
                    listOf("nope", "nah"),
                    listOf(Field.ANAT, Field.CHRISTN),
                    listOf(Misc.ABBR, Misc.DATED),
                    listOf("weird", "from TV"),
                    listOf(
                        LoanSource(
                            "a",
                            Lang.AIN,
                            LoanType.PART,
                            false
                        )
                    ),
                    listOf(Dialect.KSB, Dialect.OSB),
                    listOf(
                        Gloss(
                            "g1",
                            Lang.FRE,
                            GlossType.EXPL
                        ),
                        Gloss(
                            "g2",
                            Lang.FIN,
                            GlossType.LIT
                        )
                    )
                )
            )
        );

        val json = Json(JsonConfiguration.Stable)
        val serializer = DictEntry.serializer()
        val jsonData = json.stringify(serializer, original)
        println(jsonData)

        val deserialized = json.parse(DictEntry.serializer(), jsonData)
        assertEquals(original, deserialized)
    }
}