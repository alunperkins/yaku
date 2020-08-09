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

enum class Lang(val s: String) {
    AFR("afr"),
    AIN("ain"),
    ALG("alg"),
    AMH("amh"),
    ARA("ara"),
    ARN("arn"),
    BNT("bnt"),
    BRE("bre"),
    BUL("bul"),
    BUR("bur"),
    CHI("chi"),
    CHN("chn"),
    CZE("cze"),
    DAN("dan"),
    DUT("dut"),
    ENG("eng"),
    EPO("epo"),
    EST("est"),
    FIL("fil"),
    FIN("fin"),
    FRE("fre"),
    GEO("geo"),
    GER("ger"),
    GLG("glg"),
    GRC("grc"),
    GRE("gre"),
    HAW("haw"),
    HEB("heb"),
    HIN("hin"),
    HUN("hun"),
    ICE("ice"),
    IND("ind"),
    ITA("ita"),
    KHM("khm"),
    KOR("kor"),
    KUR("kur"),
    LAT("lat"),
    MAL("mal"),
    MAO("mao"),
    MAY("may"),
    MNC("mnc"),
    MOL("mol"),
    MON("mon"),
    NOR("nor"),
    PER("per"),
    POL("pol"),
    POR("por"),
    RUM("rum"),
    RUS("rus"),
    SAN("san"),
    SCR("scr"),
    SLO("slo"),
    SLV("slv"),
    SOM("som"),
    SPA("spa"),
    SWA("swa"),
    SWE("swe"),
    TAH("tah"),
    TAM("tam"),
    THA("tha"),
    TIB("tib"),
    TUR("tur"),
    URD("urd"),
    VIE("vie"),
    YID("yid"),
    ;

    companion object {
        private val mapping = values().associateBy(Lang::s)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Lang")
    }
}
