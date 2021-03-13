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
import org.junit.Test

internal class RomajiParserHiraganaTest {

    @Test
    fun shouldConvertEmptyStringToEmptyList() {
        assertListEquals(listOf(), RomajiParser.getHiraganaOptionsForRomaji(""))
    }

    @Test
    fun shouldConvertSingleVowel() {
        assertListEquals(listOf("あ"), RomajiParser.getHiraganaOptionsForRomaji("a"))
    }

    @Test
    fun shouldConvertTypicalMora() {
        assertListEquals(listOf("だ"), RomajiParser.getHiraganaOptionsForRomaji("da"))
    }

    @Test
    fun shouldConvertNLineMora() {
        assertListEquals(listOf("の"), RomajiParser.getHiraganaOptionsForRomaji("no"))
    }

    @Test
    fun shouldConvertThreeCharacterMora() {
        assertListEquals(listOf("ひゃ"), RomajiParser.getHiraganaOptionsForRomaji("hya"))
    }

    @Test
    fun shouldConvertMultiMoraString() {
        assertListEquals(listOf("です"), RomajiParser.getHiraganaOptionsForRomaji("desu"))
    }

    @Test
    fun shouldIgnoreSpacesBetweenMora() {
        assertListEquals(listOf("です"), RomajiParser.getHiraganaOptionsForRomaji("   de    su  "))
    }

    @Test
    fun shouldBreakMoraOnSpacesApostrophesDashes() {
        assertListEquals(listOf("にゃ"), RomajiParser.getHiraganaOptionsForRomaji("nya"))

        assertListEquals(listOf("んや"), RomajiParser.getHiraganaOptionsForRomaji("n ya"))
        assertListEquals(listOf("んや"), RomajiParser.getHiraganaOptionsForRomaji("n-ya"))
        assertListEquals(listOf("んや"), RomajiParser.getHiraganaOptionsForRomaji("n'ya"))
    }

    @Test
    fun shouldBeCaseInsensitive() {
        assertListEquals(listOf("です"), RomajiParser.getHiraganaOptionsForRomaji("DESU"))
    }

    @Test
    fun shouldThrowWithInformationOnWhatSingleThingCouldNotBeParsed() {
        assertListEquals(listOf("xx"), RomajiParser.getHiraganaOptionsForRomaji("xx"))
    }

    @Test
    fun shouldLeaveUnchangedAnythingThatCouldNotBeParsed() {
        assertListEquals(listOf("ですxxdeすyyです"), RomajiParser.getHiraganaOptionsForRomaji("desuxxdesu yy desu"))
    }

    @Test
    fun shouldConvertDoubleConsonantsToXTU() {
        assertListEquals(listOf("せっかく"), RomajiParser.getHiraganaOptionsForRomaji("sekkaku"))

        assertListEquals(listOf("ぜったい"), RomajiParser.getHiraganaOptionsForRomaji("zettai"))
    }

    @Test
    fun shouldUnderstandNN() {
        assertListEquals(listOf("ちゃわん"), RomajiParser.getHiraganaOptionsForRomaji("chawan"))
        assertListEquals(listOf("ちゃわん"), RomajiParser.getHiraganaOptionsForRomaji("chawann"))

        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("anko"))
        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("annko"))

        assertListEquals(listOf("さんぽ"), RomajiParser.getHiraganaOptionsForRomaji("sampo"))
        assertListEquals(listOf("さんぽ"), RomajiParser.getHiraganaOptionsForRomaji("sammpo"))
        assertListEquals(listOf("さんぽ"), RomajiParser.getHiraganaOptionsForRomaji("sanpo"))
        assertListEquals(listOf("さんぽ"), RomajiParser.getHiraganaOptionsForRomaji("sannpo"))
    }

    @Test
    fun shouldUnderstandNNBeforeAnNLineMora() {
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annnai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("an nai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("an-nai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("an'nai"))
    }

    @Test
    fun shouldNotReturnMultipleConsecutiveNNsMidWordNoMatterHowManyConsecutiveRomajiNsThereAreMidWord() {
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annnai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annnnai"))
        assertListEquals(listOf("あんない"), RomajiParser.getHiraganaOptionsForRomaji("annnnnai"))

        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("anko"))
        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("annko"))
        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("annnko"))
        assertListEquals(listOf("あんこ"), RomajiParser.getHiraganaOptionsForRomaji("annnnko"))
    }

    @Test
    fun shouldReturnMultiplePossibilities() {
        assertListEquals(listOf("あずじゅあ", "あずぢゅあ", "あづじゅあ", "あづぢゅあ"), RomajiParser.getHiraganaOptionsForRomaji("azujua"))
    }
}