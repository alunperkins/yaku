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
            return str.toCharArray().all(::isKana)
        }

        /**
         * by "is kanji" we mean is standard Japanese text, i.e. a mixture of kanji and kana etc.
         * (as opposed to being purely kana, which is not generally how Japanese is written except for teaching/explanatory purposes)
         * this aligns with what the dictionary means when it lists the "kana" and "kanji" forms of words
         */
        fun isKanji(str: String): Boolean {
            return str.toCharArray().any { isKana(it) }
                    && str.toCharArray().any { !isKana(it) }
        }

        fun isLatin(str: String): Boolean {
            return str.matches(Regex("[a-zA-Z0-9 ,.?!()-/]*"))
        }

        private fun isKana(c: Char): Boolean {
            val ub = Character.UnicodeBlock.of(c)
            return ub === Character.UnicodeBlock.HIRAGANA ||
                    ub === Character.UnicodeBlock.KATAKANA ||
                    ub === Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS ||
                    ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ||
                    ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                    ub === Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS ||
                    ub === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
        }
    }
}