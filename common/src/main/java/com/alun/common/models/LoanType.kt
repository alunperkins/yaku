package com.alun.common.models

enum class LoanType(val s: String) {
    FULL("full"),
    PART("part"),
    ;

    companion object {
        private val mapping = values().associateBy(LoanType::s)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in LoanType")
    }
}
