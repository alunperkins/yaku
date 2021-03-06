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
package com.alun.jmdictparser.models

import com.alun.common.models.GlossType
import com.alun.common.models.Lang

/**
 * matches the <gloss> tag type from the JMDict XML, but is used to create a different Gloss type of our own
 */
data class GlossRaw(
    /**
     * non-empty string
     */
    val str: String,
    val lang: Lang,
    val type: GlossType?
    // attribute "gender" is described in the DTD of JMDict, but is not actually used anywhere within, so is not supported here
)
