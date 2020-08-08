package com.alun.common.models

enum class Dialect(val s: String, val desc: String) {
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
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Dialect")
    }
}
