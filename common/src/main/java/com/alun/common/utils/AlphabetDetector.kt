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

class AlphabetDetector {
    companion object {
        fun isKana(str: String): Boolean {
            return str.toCharArray().all { c -> isKana(c) }
        }

        fun isKanji(str: String): Boolean {
            TODO()
        }

        fun isLatin(str: String): Boolean {
            return str.matches(Regex("[a-zA-Z0-9]*"))
        }

        private fun isKana(c: Char): Boolean {
            val ub = Character.UnicodeBlock.of(c)
            val i = c.toInt();
            return ub == Character.UnicodeBlock.HIRAGANA ||
                    ub == Character.UnicodeBlock.KATAKANA ||
                    ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS ||
                    (i in 0xFF61..0xFF9F) || // section of BMP Halfwidth and Fullwidth Forms
                    (i in 0x1B000..0x1B0FF) || // SMP kana supplement
                    (i in 0x1B100..0x1B12F) || // SMP Kana Extended-A
                    (i in 0x1B130..0x1B16F) // SMP Small Kana Extension
        }

        private fun isKanji(c: Char): Boolean {
            TODO()
        }

        private fun isLatin(c: Char): Boolean {
            TODO()
        }
    }
}