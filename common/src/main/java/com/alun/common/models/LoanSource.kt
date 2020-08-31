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
data class LoanSource(
    /**
     * TODO clarify this
     * not sure what it means for the loan source text to be null (= the loan source being an empty tag)
     * there are 1816 (!) such no-text loan sources
     */
    val str: String?,
    val lang: Lang,
    /**
     * default if null: "full"
     */
    val type: LoanType?,
    val wasei: Boolean?
)