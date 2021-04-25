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

import com.alun.common.utils.AlphabetDetector.Companion.isKana
import com.alun.common.utils.AlphabetDetector.Companion.isKanji
import com.alun.common.utils.AlphabetDetector.Companion.isLatin
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AlphabetDetectorTest {

    @Test
    fun shouldDetectKana() {
        assertEquals(true, isKana("むずかしい"))
        assertEquals(false, isKana("難しい"))
        assertEquals(false, isKana("difficult"))
    }

    @Test
    fun shouldDetectLatin() {
        assertEquals(false, isLatin("むずかしい"))
        assertEquals(false, isLatin("難しい"))
        assertEquals(true, isLatin("difficult"))
        assertEquals(true, isLatin("very difficult"))
        assertEquals(true, isLatin("difficult!"))
    }

    @Test
    fun shouldDetectKanji() {
        assertEquals(true, isKanji("難"))
        assertEquals(true, isKanji("むずかしい 難しい"))
        assertEquals(false, isKana("むずかしい 難しい"))
        assertEquals(false, isLatin("むずかしい 難しい"))
        assertEquals(true, isKanji("difficult"))
    }

    @Test
    fun shouldAcceptKanaizedLatinAsKana() {
        assertEquals(true, isKana("０１２３４５６７８９"))
        assertEquals(true, isKana("ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ"))
        assertEquals(true, isKana("ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ"))
        assertEquals(true, isKana("！＂＃＄％＆＇（）＊＋，－．／"))
        assertEquals(true, isKana("：；＜＝＞？＠"))
        assertEquals(true, isKana("［＼］＾＿｀"))
        assertEquals(true, isKana("｛｜｝～"))
    }

    @Test
    fun shouldAcceptHalfWidthKatakanaAsKana() {
        assertEquals(true, isKana("ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞ"))
    }

    @Test
    fun shouldAcceptSymbolsAndPunctuationAsKana() {
        assertEquals(true, isKana("、。〃〄々〆〇〈〉《》「」『』【】〒〓〔〕〖〗〘〙〚〛〜〝〞〟〠〡〢〣〤〥〦〧〨〩〪〭〮〯〫〬〰〱〲〳〴〵〶〷〸〹〺〻〼〽〾〿"))
        assertEquals(true, isKana("｟｠｡｢｣､･"))
        assertEquals(true, isKana("゠"))
    }

    @Test
    fun shouldAcceptNormalKanaAsKana() {
        assertEquals(
            true,
            isKana("ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワヰヱヲンヴヵヶヷヸヹヺ")
        )
        assertEquals(
            true,
            isKana("ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんゔゕゖ゙゚")
        )
        assertEquals(true, isKana("・ーヽヾヿ"))
        assertEquals(true, isKana("゛゜ゝゞゟ"))
    }

}
