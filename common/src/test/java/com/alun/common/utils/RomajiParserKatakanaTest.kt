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

internal class RomajiParserKatakanaTest {

    @Test
    fun shouldConvertEmptyStringToEmptyList() {
        assertListEquals(listOf(), RomajiParser.getKatakanaOptionsForRomaji(""))
    }

    @Test
    fun shouldConvertSingleVowel() {
        assertListEquals(listOf("ア"), RomajiParser.getKatakanaOptionsForRomaji("a"))
    }

    @Test
    fun shouldConvertTypicalMora() {
        assertListEquals(listOf("ダ"), RomajiParser.getKatakanaOptionsForRomaji("da"))
    }

    @Test
    fun shouldConvertNLineMora() {
        assertListEquals(listOf("ノ"), RomajiParser.getKatakanaOptionsForRomaji("no"))
    }

    @Test
    fun shouldConvertThreeCharacterMora() {
        assertListEquals(listOf("ヒャ"), RomajiParser.getKatakanaOptionsForRomaji("hya"))
    }

    @Test
    fun shouldConvertMultiMoraString() {
        assertListEquals(listOf("デス"), RomajiParser.getKatakanaOptionsForRomaji("desu"))
    }

    @Test
    fun shouldIgnoreSpacesBetweenMora() {
        assertListEquals(listOf("デス"), RomajiParser.getKatakanaOptionsForRomaji("   de    su  "))
    }

    @Test
    fun shouldBreakMoraOnSpacesApostrophesDashes() {
        assertListEquals(listOf("ニャ"), RomajiParser.getKatakanaOptionsForRomaji("nya"))

        assertListEquals(listOf("ンヤ"), RomajiParser.getKatakanaOptionsForRomaji("n ya"))
        assertListEquals(listOf("ンヤ"), RomajiParser.getKatakanaOptionsForRomaji("n-ya"))
        assertListEquals(listOf("ンヤ"), RomajiParser.getKatakanaOptionsForRomaji("n'ya"))
    }

    @Test
    fun shouldBeCaseInsensitive() {
        assertListEquals(listOf("デス"), RomajiParser.getKatakanaOptionsForRomaji("DESU"))
    }

    @Test
    fun shouldThrowWithInformationOnWhatSingleThingCouldNotBeParsed() {
        assertListEquals(listOf("xx"), RomajiParser.getKatakanaOptionsForRomaji("xx"))
    }

    @Test
    fun shouldLeaveUnchangedAnythingThatCouldNotBeParsed() {
        assertListEquals(listOf("デスxxdeスyyデス"), RomajiParser.getKatakanaOptionsForRomaji("desuxxdesu yy desu"))
    }

    @Test
    fun shouldConvertDoubleConsonantsToXTU() {
        assertListEquals(listOf("セッカク"), RomajiParser.getKatakanaOptionsForRomaji("sekkaku"))

        assertListEquals(listOf("ゼッタイ"), RomajiParser.getKatakanaOptionsForRomaji("zettai"))
    }

    @Test
    fun shouldUnderstandNN() {
        assertListEquals(listOf("キリン"), RomajiParser.getKatakanaOptionsForRomaji("kirin"))
        assertListEquals(listOf("キリン"), RomajiParser.getKatakanaOptionsForRomaji("kirinn"))

        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("anko"))
        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("annko"))

        assertListEquals(listOf("サンポ"), RomajiParser.getKatakanaOptionsForRomaji("sampo"))
        assertListEquals(listOf("サンポ"), RomajiParser.getKatakanaOptionsForRomaji("sammpo"))
        assertListEquals(listOf("サンポ"), RomajiParser.getKatakanaOptionsForRomaji("sanpo"))
        assertListEquals(listOf("サンポ"), RomajiParser.getKatakanaOptionsForRomaji("sannpo"))
    }

    @Test
    fun shouldUnderstandNNBeforeAnNLineMora() {
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annnai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("an nai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("an-nai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("an'nai"))
    }

    @Test
    fun shouldNotReturnMultipleConsecutiveNNsMidWordNoMatterHowManyConsecutiveRomajiNsThereAreMidWord() {
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annnai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annnnai"))
        assertListEquals(listOf("アンナイ"), RomajiParser.getKatakanaOptionsForRomaji("annnnnai"))

        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("anko"))
        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("annko"))
        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("annnko"))
        assertListEquals(listOf("アンコ"), RomajiParser.getKatakanaOptionsForRomaji("annnnko"))
    }

    @Test
    fun shouldReturnMultiplePossibilities() {
        assertListEquals(listOf("アズジュア", "アズヂュア", "アヅジュア", "アヅヂュア"), RomajiParser.getKatakanaOptionsForRomaji("azujua"))
    }

    @Test
    fun shouldUseADashAsAnOptionForLongVowels() {
        assertListEquals(listOf( "デパート", "デパアト"), RomajiParser.getKatakanaOptionsForRomaji("depaato"))
        assertListEquals(listOf( "デパート", "デパアト"), RomajiParser.getKatakanaOptionsForRomaji("depa'ato"))

        assertListEquals(listOf("アルバイト"), RomajiParser.getKatakanaOptionsForRomaji("arubaito")) // assert that it doesn't try to use a dash when the *first* character is a vowel
    }
}