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
    val stagks: List<String>?, // These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
    val stagrs: List<String>?, // These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
    /**
     * part of speech
     */
    val pos: List<POS>?,
    val xrefs: List<String>?,
    val antonyms: List<String>?, // most "ant" entries have in-band separator characters "・", e.g. "<ant>難しい・むずかしい・1</ant>" but I don't know what this format is
    val fields: List<Field>?,
    val miscs: List<Misc>?,
    val infos: List<String>?,
    val loanSource: List<LoanSource>?,
    val dialect: List<Dialect>?,
    val glosses: List<Gloss>
)
