package com.alun.common.models

import kotlinx.serialization.Serializable

@Serializable
data class Sense(
    val stagks: List<String>?, // These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
    val stagrs: List<String>?, // These elements, if present, indicate that the sense is restricted to the lexeme represented by the keb and/or reb.
    /**
     * part of speech
     */
    val pos: List<POS>?,
    val xrefs: List<String>?,
    val antonyms: List<String>?, // most "ant" entries have in-band separator characters "・", e.g. "<ant>難しい・むずかしい・1</ant>" but I don't know what this format is
    val fields: List<Field>?,
    val miscs: List<Misc>?,
    val infos: List<String>?,
    val loanSource: List<LoanSource>?,
    val dialect: List<Dialect>?,
    val glosses: List<Gloss>?
)
