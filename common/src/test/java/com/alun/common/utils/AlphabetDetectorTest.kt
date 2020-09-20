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
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AlphabetDetectorTest {
    private val exampleKana = "むずかしい"
    private val exampleKanji = "難しい"
    private val exampleLatin = "difficult"

    @Test
    fun shouldDetectKana() {
        assertEquals(true, isKana(exampleKana))
        assertEquals(false, isKana(exampleKanji))
        assertEquals(false, isKana(exampleLatin))
    }
}
