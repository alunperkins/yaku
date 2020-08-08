package com.alun.common.models

enum class KanjiInfo(val s: String, val desc: String) {
    ATEJI("ateji", "ateji (phonetic) reading"),
    IRREGULAR_KANA("ik", "word containing irregular kana usage"),
    IRREGULAR_KANJI("iK", "word containing irregular kanji usage"),
    IRREGULAR_OKURIGANA("io", "irregular okurigana usage"),
    OUT_DATED_KANJI("oK", "word containing out-dated kanji"),
    ;

    companion object {
        private val mapping = values().associateBy(KanjiInfo::desc)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in KanjiInfo")
    }
}
