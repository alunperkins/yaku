package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class DictEntry(
    val id: Int, // ent_seq
    val kanjis: List<Kanji>?,
    val kanas: List<Kana>,
    val senses: List<Sense>
)

