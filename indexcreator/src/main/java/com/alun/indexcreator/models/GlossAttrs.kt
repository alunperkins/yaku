package com.alun.indexcreator.models

enum class GlossAttrs(val qName: String) {
    Lang("xml:lang"),
    Gend("g_gend"), // gender, for applicable languages
    Type("g_type"), // values are "expl", "fig", "lit"
    ;

    companion object {
        private val mapping = values().associateBy(GlossAttrs::qName)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in GlossAttrs")
    }
}