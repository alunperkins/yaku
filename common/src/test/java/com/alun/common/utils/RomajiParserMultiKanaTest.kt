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