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

enum class POS(val abbr: String, val desc: String) {
    ADJ_F("adj-f", "noun or verb acting prenominally"),
    ADJ_I("adj-i", "adjective (keiyoushi)"),
    ADJ_IX("adj-ix", "adjective (keiyoushi) - yoi/ii class"),
    ADJ_KU("adj-ku", "`ku' adjective (archaic)"),
    ADJ_NA("adj-na", "adjectival nouns or quasi-adjectives (keiyodoshi)"),
    ADJ_NARI("adj-nari", "archaic/formal form of na-adjective"),
    ADJ_NO("adj-no", "nouns which may take the genitive case particle `no'"),
    ADJ_PN("adj-pn", "pre-noun adjectival (rentaishi)"),
    ADJ_SHIKU("adj-shiku", "`shiku' adjective (archaic)"),
    ADJ_T("adj-t", "`taru' adjective"),
    ADV("adv", "adverb (fukushi)"),
    ADV_TO("adv-to", "adverb taking the `to' particle"),
    AUX_ADJ("aux-adj", "auxiliary adjective"),
    AUX("aux", "auxiliary"),
    AUX_V("aux-v", "auxiliary verb"),
    CONJ("conj", "conjunction"),
    COP("cop", "copula"),
    CTR("ctr", "counter"),
    EXP("exp", "expressions (phrases, clauses, etc.)"),
    INT("int", "interjection (kandoushi)"),
    N_ADV("n-adv", "adverbial noun (fukushitekimeishi)"),
    N("n", "noun (common) (futsuumeishi)"),
    N_PREF("n-pref", "noun, used as a prefix"),
    N_SUF("n-suf", "noun, used as a suffix"),
    N_T("n-t", "noun (temporal) (jisoumeishi)"),
    NUM("num", "numeric"),
    PN("pn", "pronoun"),
    PREF("pref", "prefix"),
    PRT("prt", "particle"),
    SUF("suf", "suffix"),
    UNC("unc", "unclassified"),
    V1("v1", "Ichidan verb"),
    V1_S("v1-s", "Ichidan verb - kureru special class"),
    V2A_S("v2a-s", "Nidan verb with 'u' ending (archaic)"),
    V2B_K("v2b-k", "Nidan verb (upper class) with `bu' ending (archaic)"),
    V2D_S("v2d-s", "Nidan verb (lower class) with `dzu' ending (archaic)"),
    V2G_K("v2g-k", "Nidan verb (upper class) with `gu' ending (archaic)"),
    V2G_S("v2g-s", "Nidan verb (lower class) with `gu' ending (archaic)"),
    V2H_K("v2h-k", "Nidan verb (upper class) with `hu/fu' ending (archaic)"),
    V2H_S("v2h-s", "Nidan verb (lower class) with `hu/fu' ending (archaic)"),
    V2K_K("v2k-k", "Nidan verb (upper class) with `ku' ending (archaic)"),
    V2K_S("v2k-s", "Nidan verb (lower class) with `ku' ending (archaic)"),
    V2M_S("v2m-s", "Nidan verb (lower class) with `mu' ending (archaic)"),
    V2N_S("v2n-s", "Nidan verb (lower class) with `nu' ending (archaic)"),
    V2R_K("v2r-k", "Nidan verb (upper class) with `ru' ending (archaic)"),
    V2R_S("v2r-s", "Nidan verb (lower class) with `ru' ending (archaic)"),
    V2S_S("v2s-s", "Nidan verb (lower class) with `su' ending (archaic)"),
    V2T_K("v2t-k", "Nidan verb (upper class) with `tsu' ending (archaic)"),
    V2T_S("v2t-s", "Nidan verb (lower class) with `tsu' ending (archaic)"),
    V2W_S("v2w-s", "Nidan verb (lower class) with `u' ending and `we' conjugation (archaic)"),
    V2Y_K("v2y-k", "Nidan verb (upper class) with `yu' ending (archaic)"),
    V2Y_S("v2y-s", "Nidan verb (lower class) with `yu' ending (archaic)"),
    V2Z_S("v2z-s", "Nidan verb (lower class) with `zu' ending (archaic)"),
    V4B("v4b", "Yodan verb with `bu' ending (archaic)"),
    V4G("v4g", "Yodan verb with `gu' ending (archaic)"),
    V4H("v4h", "Yodan verb with `hu/fu' ending (archaic)"),
    V4K("v4k", "Yodan verb with `ku' ending (archaic)"),
    V4M("v4m", "Yodan verb with `mu' ending (archaic)"),
    V4R("v4r", "Yodan verb with `ru' ending (archaic)"),
    V4S("v4s", "Yodan verb with `su' ending (archaic)"),
    V4T("v4t", "Yodan verb with `tsu' ending (archaic)"),
    V5ARU("v5aru", "Godan verb - -aru special class"),
    V5B("v5b", "Godan verb with `bu' ending"),
    V5G("v5g", "Godan verb with `gu' ending"),
    V5K("v5k", "Godan verb with `ku' ending"),
    V5K_S("v5k-s", "Godan verb - Iku/Yuku special class"),
    V5M("v5m", "Godan verb with `mu' ending"),
    V5N("v5n", "Godan verb with `nu' ending"),
    V5R_I("v5r-i", "Godan verb with `ru' ending (irregular verb)"),
    V5R("v5r", "Godan verb with `ru' ending"),
    V5S("v5s", "Godan verb with `su' ending"),
    V5T("v5t", "Godan verb with `tsu' ending"),
    V5U("v5u", "Godan verb with `u' ending"),
    V5U_S("v5u-s", "Godan verb with `u' ending (special class)"),
    VI("vi", "intransitive verb"),
    VK("vk", "Kuru verb - special class"),
    VN("vn", "irregular nu verb"),
    VR("vr", "irregular ru verb, plain form ends with -ri"),
    VS_C("vs-c", "su verb - precursor to the modern suru"),
    VS_I("vs-i", "suru verb - included"),
    VS("vs", "noun or participle which takes the aux. verb suru"),
    VS_S("vs-s", "suru verb - special class"),
    VT("vt", "transitive verb"),
    VZ("vz", "Ichidan verb - zuru verb (alternative form of -jiru verbs)"),
    ;

    companion object {
        private val mapping = values().associateBy(POS::desc)
        fun fromStr(desc: String) = mapping[desc] ?: error("Look up failed for \"$desc\" in POS")
    }
}