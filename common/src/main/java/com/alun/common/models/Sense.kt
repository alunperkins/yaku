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
data class Sense(
    /**
     * These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
     * null or non-empty list
     */
    val stagks: List<String>?,
    /**
     * These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
     * null or non-empty list
     */
    val stagrs: List<String>?,
    /**
     * part of speech
     * null or non-empty list
     */
    val pos: List<POS>?,
    /**
     * null or non-empty list
     */
    val xrefs: List<Reference>?,
    /**
     * null or non-empty list
     */
    val antonyms: List<Reference>?,
    /**
     * null or non-empty list
     */
    val fields: List<Field>?,
    /**
     * null or non-empty list
     */
    val miscs: List<Misc>?,
    /**
     * null or non-empty list
     */
    val infos: List<String>?,
    /**
     * null or non-empty list
     */
    val loanSource: List<LoanSource>?,
    /**
     * null or non-empty list
     */
    val dialect: List<Dialect>?,
    /**
     * non-empty list
     */
    val glosses: List<Gloss>
)