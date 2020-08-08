package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Kanji(
    val str: String,
    val infos: List<KanjiInfo>?,
    val priorities: List<Priority>?
)
