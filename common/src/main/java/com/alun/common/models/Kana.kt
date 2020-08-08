package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Kana(
    val str: String,
    val infos: List<ReadingInfo>?,
    val priorities: List<Priority>?,
    val onlyForKanjis: List<String>?,
    val noKanji: Boolean?
)