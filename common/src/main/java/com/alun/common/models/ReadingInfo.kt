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
package com.alun.common.models

enum class ReadingInfo(val s/*TODO rename to abbr*/: String, val desc: String) {
    IRREGULAR_KANA("ik", "word containing irregular kana usage"),
    OUT_DATED_KANA("ok", "out-dated or obsolete kana usage"),
    GIKUN("gikun", "gikun (meaning as reading) or jukujikun (special kanji reading)"),
    ;

    companion object {
        private val mapping = values().associateBy(ReadingInfo::desc)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in ReadingInfo")
    }
}
