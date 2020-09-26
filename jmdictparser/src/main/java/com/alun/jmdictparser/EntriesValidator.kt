/*
    Yaku offline browser of Japanese dictionaries
    Copyright (C) 2020 Alun Perkins

    This file is part of Yaku.

    Yaku is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Yaku is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Yaku.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.alun.jmdictparser

import com.alun.common.models.*
import com.alun.jmdictparser.models.DictEntryRaw
import com.alun.jmdictparser.models.GlossRaw
import com.alun.jmdictparser.models.SenseRaw

fun DictEntryRaw.validateThatAllNonNullMembersAreNonEmpty() {
    if (kanjis != null && kanjis.isEmpty()) error("non-null member should be non-empty")
    if (kanas.isEmpty()) error("non-null member should be non-empty")
    if (senses.isEmpty()) error("non-null member should be non-empty")
}

fun GlossRaw.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) error("non-null member should be non-empty")
}

fun Kana.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) error("non-null member should be non-empty")
    if (priorities != null && priorities!!.isEmpty()) error("non-null member should be non-empty")
    if (onlyForKanjis != null && onlyForKanjis!!.isEmpty()) error("non-null member should be non-empty")
    if (str.isEmpty()) error("non-null member should be non-empty")
}

fun Kanji.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) error("non-null member should be non-empty")
    if (priorities != null && priorities!!.isEmpty()) error("non-null member should be non-empty")
}

fun LoanSource.validateThatAllNonNullMembersAreNonEmpty() {
    if (str != null && str!!.isEmpty()) error("non-null member should be non-empty")
}

fun SenseRaw.validateThatAllNonNullMembersAreNonEmpty() {
    if (stagks != null && stagks.isEmpty()) error("non-null member should be non-empty")
    if (stagrs != null && stagrs.isEmpty()) error("non-null member should be non-empty")
    if (pos != null && pos.isEmpty()) error("non-null member should be non-empty")
    if (xrefs != null && xrefs.isEmpty()) error("non-null member should be non-empty")
    if (antonyms != null && antonyms.isEmpty()) error("non-null member should be non-empty")
    if (fields != null && fields.isEmpty()) error("non-null member should be non-empty")
    if (miscs != null && miscs.isEmpty()) error("non-null member should be non-empty")
    if (infos != null && infos.isEmpty()) error("non-null member should be non-empty")
    if (loanSource != null && loanSource.isEmpty()) error("non-null member should be non-empty")
    if (dialect != null && dialect.isEmpty()) error("non-null member should be non-empty")
    if (glosses.isEmpty()) error("non-null member should be non-empty")
}

class EntriesValidator {
    fun validateThatAllNonNullMembersAreNonEmpty(entries: List<DictEntryRaw>) {
        entries.forEach { entry ->
            entry.validateThatAllNonNullMembersAreNonEmpty()
            entry.kanjis?.forEach { kanji -> kanji.validateThatAllNonNullMembersAreNonEmpty() }
            entry.kanas.forEach { kana -> kana.validateThatAllNonNullMembersAreNonEmpty() }
            entry.senses.forEach { sense ->
                sense.validateThatAllNonNullMembersAreNonEmpty()
                sense.loanSource?.forEach { ls -> ls.validateThatAllNonNullMembersAreNonEmpty() }
                sense.glosses.forEach { gloss -> gloss.validateThatAllNonNullMembersAreNonEmpty() }
            }
        }
    }

    fun checkInternalReferentialIntegrityOfEachEntry(entries: List<DictEntryRaw>) {
        entries.forEach { checkEntryInternalReferenceIntegrity(it) }
    }

    private fun checkEntryInternalReferenceIntegrity(entry: DictEntryRaw) {
        val entryKanjiStrings: List<String> = entry.kanjis?.map { it.str } ?: listOf()
        val entryKanaStrings: List<String> = entry.kanas.map { it.str }

        // check re_restr references
        entry.kanas
            .stream()
            .filter { it.onlyForKanjis != null }
            .flatMap { it.onlyForKanjis!!.stream() }
            .forEach { referencedKanji ->
                val brokenReference = !entryKanjiStrings.contains(referencedKanji)
                if (brokenReference) {
                    error("Entry ${entry.id} has a broken re_restr ref \"$referencedKanji\"")
                }
            }

        // check stagk references
        entry.senses.stream()
            .filter { it.stagks != null }
            .flatMap { it.stagks!!.stream() }
            .forEach { referencedKanji ->
                val brokenReference = !entryKanjiStrings.contains(referencedKanji)
                if (brokenReference) {
                    error("Entry ${entry.id} has broken stagk ref \"$referencedKanji\"")
                }
            }

        // check stagr references
        entry.senses.stream()
            .filter { it.stagrs != null }
            .flatMap { it.stagrs!!.stream() }
            .forEach { referencedKana ->
                val brokenReference = !entryKanaStrings.contains(referencedKana)
                if (brokenReference) {
                    error("Entry ${entry.id} has broken stagt ref \"$referencedKana\"")
                }
            }
    }

    fun checkSampleEntry(entries: List<DictEntryRaw>) {
        // some entry I typed out by looking at the JMDict XML manually
        val entryCheckExpected = DictEntryRaw(
            1038660,
            listOf(Kanji("空オケ", null, null)),
            listOf(
                Kana("からオケ", null, null, null, null),
                Kana("カラオケ", null, listOf(Priority.ICHI1, Priority.SPEC1), null, true)
            ),
            listOf(
                SenseRaw(
                    null,
                    null,
                    listOf(POS.N),
                    null,
                    null,
                    null,
                    listOf(Misc.UK),
                    listOf("from 空 and オーケストラ"),
                    null,
                    null,
                    listOf(GlossRaw("karaoke", Lang.ENG, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("karaoke", Lang.DUT, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("karaoké", Lang.FRE, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("Karaoke (wörtl. leeres Orchester)", Lang.GER, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("караоке", Lang.RUS, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("karaoke (petje s posneto spremljavo)", Lang.SLV, null))
                ),
                SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(
                        GlossRaw("karaoke (cantar música grabada)", Lang.SPA, null),
                        GlossRaw("karaoke", Lang.SPA, null)
                    )
                ), SenseRaw(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(GlossRaw("karaoke", Lang.SWE, null))
                )

            )
        )
        val entryCheckActual = entries[3445]
        if (entryCheckActual != entryCheckExpected) error("possible parsing error")
    }
}
