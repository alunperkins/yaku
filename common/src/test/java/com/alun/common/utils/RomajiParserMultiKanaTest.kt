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
package com.alun.common.utils

import com.alun.common.TestUtils.Companion.assertListEquals
import org.junit.Assert
import org.junit.Test

internal class RomajiParserMultiKanaTest {

    @Test
    fun shouldReturnHiraganaAndKatakanaOptions() {
        assertListEquals(listOf("ごみばこ", "ゴミバコ"), RomajiParser.getAllKanaOptionsForRomaji("gomibako"))
    }

    @Test
    fun shouldLeaveUnchangedAnyNonRomajiInTheInput() {
        assertListEquals(listOf("ゴミばこ", "ゴミバコ"), RomajiParser.getAllKanaOptionsForRomaji("ゴミbako"))
        assertListEquals(listOf("ごみ箱", "ゴミ箱"), RomajiParser.getAllKanaOptionsForRomaji("gomi箱"))
        assertListEquals(listOf("ゴミ箱", "ゴミ箱"), RomajiParser.getAllKanaOptionsForRomaji("ゴミ箱")) // both hiragana and katakana converters leave it totally unchaged, resulting in two copies. I guess this is fine.
    }

    @Test
    fun itIsOKForItNotToUnderstandLongVowelsThatAreInputWithMixedAlphabets() {
        val actual = RomajiParser.getKatakanaOptionsForRomaji("デパato")
        assertListEquals(listOf("デパアト"), actual)
        Assert.assertFalse(actual.contains("デパート"))
    }

}