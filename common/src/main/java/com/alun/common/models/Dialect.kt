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

enum class Dialect(val abbr: String, val desc: String) {
    HOB("hob", "Hokkaido-ben"),
    KSB("ksb", "Kansai-ben"),
    KTB("ktb", "Kantou-ben"),
    KYB("kyb", "Kyoto-ben"),
    KYU("kyu", "Kyuushuu-ben"),
    NAB("nab", "Nagano-ben"),
    OSB("osb", "Osaka-ben"),
    RKB("rkb", "Ryuukyuu-ben"),
    THB("thb", "Touhoku-ben"),
    TSB("tsb", "Tosa-ben"),
    TSUG("tsug", "Tsugaru-ben"),
    ;

    companion object {
        private val mapping = values().associateBy(Dialect::desc)
        fun fromStr(desc: String) = mapping[desc] ?: error("Look up failed for \"$desc\" in Dialect")
    }
}
