package com.alun.common.models

enum class Misc(val s: String, val desc: String) {
    ABBR("abbr", "abbreviation"),
    ARCH("arch", "archaism"),
    CHN("chn", "children's language"),
    COL("col", "colloquialism"),
    DATED("dated", "dated term"),
    DEROG("derog", "derogatory"),
    FAM("fam", "familiar language"),
    FEM("fem", "female term or language"),
    HIST("hist", "historical term"),
    HON("hon", "honorific or respectful (sonkeigo) language"),
    HUM("hum", "humble (kenjougo) language"),
    ID("id", "idiomatic expression"),
    JOC("joc", "jocular, humorous term"),
    LITF("litf", "literary or formal term"),
    MALE("male", "male term or language"),
    M_SL("m-sl", "manga slang"),
    NET_SL("net-sl", "Internet slang"),
    OBSC("obsc", "obscure term"),
    OBS("obs", "obsolete term"),
    ON_MIM("on-mim", "onomatopoeic or mimetic word"),
    PERSON("person", "full name of a particular person"),
    PLACE("place", "place name"),
    POET("poet", "poetical term"),
    POL("pol", "polite (teineigo) language"),
    PROVERB("proverb", "proverb"),
    QUOTE("quote", "quotation"),
    RARE("rare", "rare"),
    SENS("sens", "sensitive"),
    SL("sl", "slang"),
    UK("uk", "word usually written using kana alone"),
    VULG("vulg", "vulgar expression or word"),
    WORK("work", "work of art, literature, music, etc. name"),
    YOJI("yoji", "yojijukugo"),
    ;

    companion object {
        private val mapping = values().associateBy(Misc::desc)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Misc")
    }
}
