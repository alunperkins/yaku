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

enum class Misc(val s/*TODO rename to abbr*/: String, val desc: String) {
    ABBR("abbr", "abbreviation"),
    ARCH("arch", "archaism"),
    CHN("chn", "children's language"),
    COL("col", "colloquialism"),
    DATED("dated", "dated term"),
    DEROG("derog", "derogatory"),
    FAM("fam", "familiar language"),
    FEM("fem", "female term or language"),
    HIST("hist", "historical term"),
    HON("hon", "honorific or respectful (sonkeigo) language"),
    HUM("hum", "humble (kenjougo) language"),
    ID("id", "idiomatic expression"),
    JOC("joc", "jocular, humorous term"),
    LITF("litf", "literary or formal term"),
    MALE("male", "male term or language"),
    M_SL("m-sl", "manga slang"),
    NET_SL("net-sl", "Internet slang"),
    OBSC("obsc", "obscure term"),
    OBS("obs", "obsolete term"),
    ON_MIM("on-mim", "onomatopoeic or mimetic word"),
    PERSON("person", "full name of a particular person"),
    PLACE("place", "place name"),
    POET("poet", "poetical term"),
    POL("pol", "polite (teineigo) language"),
    PROVERB("proverb", "proverb"),
    QUOTE("quote", "quotation"),
    RARE("rare", "rare"),
    SENS("sens", "sensitive"),
    SL("sl", "slang"),
    UK("uk", "word usually written using kana alone"),
    VULG("vulg", "vulgar expression or word"),
    WORK("work", "work of art, literature, music, etc. name"),
    YOJI("yoji", "yojijukugo"),
    ;

    companion object {
        private val mapping = values().associateBy(Misc::desc)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Misc")
    }
}
