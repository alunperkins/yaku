package com.alun.common.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DictEntryTest {
    @Test
    fun serialization() {
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