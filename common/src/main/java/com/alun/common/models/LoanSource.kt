package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class LoanSource(
    /**
     * not sure what it means for the loan source text to be null (= the loan source being an empty tag)
     * there are 1816 (!) such no-text loan sources
     */
    val str: String?,
    val lang: Lang,
    /**
     * default if null: "full"
     */
    val type: LoanType?,
    val wasei: Boolean?
)
