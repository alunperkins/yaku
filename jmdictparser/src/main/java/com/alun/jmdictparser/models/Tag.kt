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

enum class Tag(val tag: String) {
    JMDict("JMdict"),
    Entry("entry"),
    EntSeq("ent_seq"),
    Kanji("k_ele"),
    KanjiValue("keb"),
    KanjiInfo("ke_inf"),
    KanjiPri("ke_pri"),
    Kana("r_ele"),
    KanaValue("reb"),
    KanaInfo("re_inf"),
    KanaPri("re_pri"),
    KanaRestr("re_restr"),
    KanaNoKanji("re_nokanji"),
    Sense("sense"),
    SenseTagk("stagk"),
    SenseTagr("stagr"),
    SensePOS("pos"),
    SenseXRef("xref"),
    SenseAnt("ant"),
    SenseField("field"),
    SenseMisc("misc"),
    SenseInfo("s_inf"),
    SenseLSource("lsource"),
    SenseDialect("dial"),
    SenseGloss("gloss"),
    ;

    companion object {
        private val mapping = values().associateBy(Tag::tag)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Tag")
    }
}