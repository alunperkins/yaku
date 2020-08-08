package com.alun.common.models

enum class ReadingInfo(val s: String, val desc: String) {
    IRREGULAR_KANA("ik", "word containing irregular kana usage"),
    OUT_DATED_KANA("ok", "out-dated or obsolete kana usage"),
    GIKUN("gikun", "gikun (meaning as reading) or jukujikun (special kanji reading)"),
    ;

    companion object {
        private val mapping = values().associateBy(ReadingInfo::desc)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in ReadingInfo")
    }
}
