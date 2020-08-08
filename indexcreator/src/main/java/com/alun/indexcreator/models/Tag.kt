package com.alun.indexcreator.models

enum class Tag(val tag: String) {
    JMDict("JMdict"),
    Entry("entry"),
    EntSeq("ent_seq"),
    Kanji("k_ele"),
    KanjiValue("keb"),
    KanjiInfo("ke_inf"),
    KanjiPri("ke_pri"),
    Kana("r_ele"),
    KanaValue("reb"),
    KanaInfo("re_inf"),
    KanaPri("re_pri"),
    KanaRestr("re_restr"),
    KanaNoKanji("re_nokanji"),
    Sense("sense"),
    SenseTagk("stagk"),
    SenseTagr("stagr"),
    SensePOS("pos"),
    SenseXRef("xref"),
    SenseAnt("ant"),
    SenseField("field"),
    SenseMisc("misc"),
    SenseInfo("s_inf"),
    SenseLSource("lsource"),
    SenseDialect("dial"),
    SenseGloss("gloss"),
    ;

    companion object {
        private val mapping = values().associateBy(Tag::tag)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Tag")
    }
}