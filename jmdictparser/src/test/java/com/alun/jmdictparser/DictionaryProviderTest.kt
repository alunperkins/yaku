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
package com.alun.jmdictparser

import com.alun.common.models.*
import com.alun.jmdictparser.models.DictEntryRaw
import com.alun.jmdictparser.models.GlossRaw
import com.alun.jmdictparser.models.SenseRaw
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

internal class DictionaryProviderTest {
    private fun getTks(int: Int): List<String> {
        return listOf("stagk${int}1", "stagk${int}2")
    }

    private fun getTrs(int: Int): List<String> {
        return listOf("stagr${int}1", "stagr${int}2")
    }

    private fun getXRs(int: Int): List<Reference> {
        return listOf(Reference("xrefk${int}1", "xrefr${int}1", 0), Reference("xrefk${int}2", "xrefr${int}2", 0))
    }

    private fun getAs(int: Int): List<Reference> {
        return listOf(Reference("antk${int}1", "antr${int}1", 0), Reference("antk${int}2", "antr${int}2", 0))
    }

    private fun getFs(int: Int): List<Field> {
        return listOf(Field.values()[int])
    }

    private fun getIs(int: Int): List<String> {
        return listOf("info${int}1", "info${int}2")
    }

    private fun getLSs(int: Int): List<LoanSource> {
        return listOf(LoanSource("ls${int}", Lang.RUS, null, null))
    }

    private fun getDs(int: Int): List<Dialect> {
        return listOf(Dialect.values()[int])
    }

    @Test
    fun shouldCarryPossAndMiscsForwardWithinALanguage() {
        // input has list of senses with pos/misc-lang: ENG-A, ENG-null, ENG-B, DAN-null,
        val input = DictEntryRaw(
            1234,
            listOf(Kanji("k1", null, null), Kanji("k2", null, null)),
            listOf(Kana("r1", null, null, null, null), Kana("r2", null, null, null, null)),
            listOf(
                SenseRaw(
                    getTks(1), getTrs(1),
                    listOf(POS.N, POS.ADJ_F),
                    getXRs(1), getAs(1), getFs(1),
                    listOf(Misc.UK, Misc.ABBR),
                    getIs(1), getLSs(1), getDs(1),
                    listOf(
                        GlossRaw("g11", Lang.ENG, null),
                        GlossRaw("g12", Lang.ENG, null)
                    )
                ),
                SenseRaw(
                    getTks(2), getTrs(2),
                    null,
                    getXRs(2), getAs(2), getFs(2),
                    null,
                    getIs(2), getLSs(2), getDs(2),
                    listOf(
                        GlossRaw("g21", Lang.ENG, null),
                        GlossRaw("g22", Lang.ENG, null)
                    )
                ),
                SenseRaw(
                    getTks(3), getTrs(3),
                    listOf(POS.ADJ_IX, POS.ADJ_NARI),
                    getXRs(3), getAs(3), getFs(3),
                    listOf(Misc.COL, Misc.DATED),
                    getIs(3), getLSs(3), getDs(3),
                    listOf(
                        GlossRaw("g31", Lang.ENG, null),
                        GlossRaw("g32", Lang.ENG, null)
                    )
                ),
                SenseRaw(
                    getTks(4), getTrs(4),
                    null,
                    getXRs(4), getAs(4), getFs(4),
                    null,
                    getIs(4), getLSs(4), getDs(4),
                    listOf(
                        GlossRaw("g41", Lang.DAN, null),
                        GlossRaw("g42", Lang.DAN, null)
                    )
                )
            )
        )

        val outputActual = DictionaryProvider().convertToOurModelTypes(listOf(input))
        // input has list of senses with pos/misc-lang: ENG-A, ENG-A, ENG-B, DAN-null,
        val outputExpected = listOf(
            DictEntry(
                1234,
                listOf(Kanji("k1", null, null), Kanji("k2", null, null)),
                listOf(Kana("r1", null, null, null, null), Kana("r2", null, null, null, null)),
                listOf(
                    Sense(
                        getTks(1), getTrs(1), Lang.ENG,
                        listOf(POS.N, POS.ADJ_F),
                        getXRs(1), getAs(1), getFs(1),
                        listOf(Misc.UK, Misc.ABBR),
                        getIs(1), getLSs(1), getDs(1),
                        listOf(
                            Gloss("g11", null),
                            Gloss("g12", null)
                        )
                    ),
                    Sense(
                        getTks(2), getTrs(2), Lang.ENG,
                        listOf(POS.N, POS.ADJ_F),
                        getXRs(2), getAs(2), getFs(2),
                        listOf(Misc.UK, Misc.ABBR),
                        getIs(2), getLSs(2), getDs(2),
                        listOf(
                            Gloss("g21", null),
                            Gloss("g22", null)
                        )
                    ),
                    Sense(
                        getTks(3), getTrs(3), Lang.ENG,
                        listOf(POS.ADJ_IX, POS.ADJ_NARI),
                        getXRs(3), getAs(3), getFs(3),
                        listOf(Misc.COL, Misc.DATED),
                        getIs(3), getLSs(3), getDs(3),
                        listOf(
                            Gloss("g31", null),
                            Gloss("g32", null)
                        )
                    ),
                    Sense(
                        getTks(4), getTrs(4), Lang.DAN,
                        null,
                        getXRs(4), getAs(4), getFs(4),
                        null,
                        getIs(4), getLSs(4), getDs(4),
                        listOf(
                            Gloss("g41", null),
                            Gloss("g42", null)
                        )
                    )
                )
            )
        )
        assertEquals(outputExpected, outputActual)
    }

    @Test
    fun shouldFailLoudlyIfLanguagesAreMixedUp() {
        // input has list of senses with pos/misc-lang: ENG-A, DAN-null, ENG-B
        val input = DictEntryRaw(
            1234,
            listOf(Kanji("k1", null, null), Kanji("k2", null, null)),
            listOf(Kana("r1", null, null, null, null), Kana("r2", null, null, null, null)),
            listOf(
                SenseRaw(
                    getTks(1), getTrs(1),
                    listOf(POS.N, POS.ADJ_F),
                    getXRs(1), getAs(1), getFs(1),
                    listOf(Misc.UK, Misc.ABBR),
                    getIs(1), getLSs(1), getDs(1),
                    listOf(
                        GlossRaw("g11", Lang.ENG, null),
                        GlossRaw("g12", Lang.ENG, null)
                    )
                ),
                SenseRaw(
                    getTks(2), getTrs(2),
                    null,
                    getXRs(2), getAs(2), getFs(2),
                    null,
                    getIs(2), getLSs(2), getDs(2),
                    listOf(
                        GlossRaw("g21", Lang.DAN, null),
                        GlossRaw("g22", Lang.DAN, null)
                    )
                ),
                SenseRaw(
                    getTks(3), getTrs(3),
                    listOf(POS.ADJ_IX, POS.ADJ_NARI),
                    getXRs(3), getAs(3), getFs(3),
                    listOf(Misc.COL, Misc.DATED),
                    getIs(3), getLSs(3), getDs(3),
                    listOf(
                        GlossRaw("g31", Lang.ENG, null),
                        GlossRaw("g32", Lang.ENG, null)
                    )
                )
            )
        )

        val e =
            assertThrows(RuntimeException::class.java) { DictionaryProvider().convertToOurModelTypes(listOf(input)) }
        assertEquals("Assumption broken: senses are assumed to be grouped by language", e.message)
    }

    @Test
    fun shouldFailLoudlyIfNonSupportedLangHasAPos() {
        // input has list of senses with pos/misc-lang: DAN-A
        val input = DictEntryRaw(
            1234,
            listOf(Kanji("k1", null, null), Kanji("k2", null, null)),
            listOf(Kana("r1", null, null, null, null), Kana("r2", null, null, null, null)),
            listOf(
                SenseRaw(
                    getTks(1), getTrs(1),
                    listOf(POS.N, POS.ADJ_F),
                    getXRs(1), getAs(1), getFs(1),
                    listOf(Misc.UK, Misc.ABBR),
                    getIs(1), getLSs(1), getDs(1),
                    listOf(
                        GlossRaw("g11", Lang.DAN, null),
                        GlossRaw("g12", Lang.DAN, null)
                    )
                )
            )
        )

        val e =
            assertThrows(RuntimeException::class.java) { DictionaryProvider().convertToOurModelTypes(listOf(input)) }
        assertEquals(
            "Assumption broken: found POS info for a sense with supposedly non-pos-supported language DAN",
            e.message
        )
    }

    @Test
    fun shouldFailLoudlyIfSupportedLangDoesNotHaveAPos() {
        // input has list of senses with pos/misc-lang: ENG-null
        val input = DictEntryRaw(
            1234,
            listOf(Kanji("k1", null, null), Kanji("k2", null, null)),
            listOf(Kana("r1", null, null, null, null), Kana("r2", null, null, null, null)),
            listOf(
                SenseRaw(
                    getTks(1), getTrs(1),
                    null,
                    getXRs(1), getAs(1), getFs(1),
                    null,
                    getIs(1), getLSs(1), getDs(1),
                    listOf(
                        GlossRaw("g11", Lang.ENG, null),
                        GlossRaw("g12", Lang.ENG, null)
                    )
                )
            )
        )

        val e =
            assertThrows(RuntimeException::class.java) { DictionaryProvider().convertToOurModelTypes(listOf(input)) };
        assertEquals(
            "Assumption broken: found no POS info for a sense with supposedly pos-supported language ENG",
            e.message
        )
    }
}