package com.alun.common.models

enum class GlossType(val s: String) {
    EXPL("expl"),
    FIG("fig"),
    LIT("lit"),
    ;

    companion object {
        private val mapping = values().associateBy(GlossType::s)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in GlossType")
    }
}
