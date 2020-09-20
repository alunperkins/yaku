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

import kotlinx.serialization.Serializable

@Serializable
data class Reference(
    /**
     * a string exactly matching a kanji string of the referenced word, or null if absent
     */
    val kanji: String?,
    /**
     * a string exactly matching a kana string of the referenced word, or null if absent
     */
    val kana: String?,
    /**
     * the number of the referenced sense of the referenced word, where its senses are numbered in the order of
     * appearance in the XML, starting from 1.
     * Since we trim out senses with no glosses, it may very very rarely happen that this sense ref is broken.
     */
    val senseNo: Int?
)