package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Gloss(
    val str: String,
    val lang: Lang,
    val type: GlossType?
)
