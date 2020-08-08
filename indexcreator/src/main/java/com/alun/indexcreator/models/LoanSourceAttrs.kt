package com.alun.indexcreator.models

enum class LoanSourceAttrs(val qName: String) {
    Lang("xml:lang"),
    Type("ls_type"), // when present its value is always "part", absence means "full"
    Wasei("ls_wasei"), // when present its value is always "y"
    ;

    companion object {
        private val mapping = values().associateBy(LoanSourceAttrs::qName)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in LoanSourceAttrs")
    }
}