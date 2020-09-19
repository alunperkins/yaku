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
package com.alun.indexcreator

import com.alun.common.models.*
import javax.xml.parsers.SAXParserFactory

fun DictEntry.validateThatAllNonNullMembersAreNonEmpty() {
    if (kanjis != null && kanjis!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (kanas.isEmpty()) throw Error("non-null member should be non-empty")
    if (senses.isEmpty()) throw Error("non-null member should be non-empty")
}

fun Gloss.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) throw Error("non-null member should be non-empty")
}

fun Kana.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) throw Error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (priorities != null && priorities!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (onlyForKanjis != null && onlyForKanjis!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (str.isEmpty()) throw Error("non-null member should be non-empty")
}

fun Kanji.validateThatAllNonNullMembersAreNonEmpty() {
    if (str.isEmpty()) throw Error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (priorities != null && priorities!!.isEmpty()) throw Error("non-null member should be non-empty")
}

fun LoanSource.validateThatAllNonNullMembersAreNonEmpty() {
    if (str != null && str!!.isEmpty()) throw Error("non-null member should be non-empty")
}

fun Sense.validateThatAllNonNullMembersAreNonEmpty() {
    if (stagks != null && stagks!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (stagrs != null && stagrs!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (pos != null && pos!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (xrefs != null && xrefs!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (antonyms != null && antonyms!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (fields != null && fields!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (miscs != null && miscs!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (loanSource != null && loanSource!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (dialect != null && dialect!!.isEmpty()) throw Error("non-null member should be non-empty")
    if (glosses.isEmpty()) throw Error("non-null member should be non-empty")
}

class JMDictParser {
    private val antonymInBandDelimiter = '・' // TODO move to model class?

    fun run(path: String): List<DictEntry> {
        val handler = JMDictHandler()
        SAXParserFactory.newInstance().newSAXParser().parse(path, handler)

        val entries = handler.entries

        checkSample(entries)

        validateThatAllNonNullMembersAreNonEmpty(entries)
        checkReferentialIntegrity(entries)

        return entries
    }

    private fun validateThatAllNonNullMembersAreNonEmpty(entries: List<DictEntry>) {
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

    private fun checkReferentialIntegrity(entries: List<DictEntry>) {
        entries.forEach { entry ->
            // check re_restr references
            val entryKanjiStrings: List<String> = entry.kanjis?.map { it.str } ?: listOf()
            val entryKanaStrings: List<String> = entry.kanas.map { it.str }
            entry.kanas
                .stream()
                .filter { it.onlyForKanjis != null }
                .flatMap { it.onlyForKanjis!!.stream() }
                .forEach { referencedKanji ->
                    val brokenReference = !entryKanjiStrings.contains(referencedKanji)
                    if (brokenReference) {
                        println("Warning: entry ${entry.id} has a broken re_restr ref $referencedKanji")
                    }
                }

            // check stagk references
            entry.senses.stream()
                .filter { it.stagks != null }
                .flatMap { it.stagks!!.stream() }
                .forEach { referencedKanji ->
                    val brokenReference = !entryKanjiStrings.contains(referencedKanji)
                    if (brokenReference) {
                        println("Warning: entry ${entry.id} has broken stagk ref $referencedKanji")
                    }
                }

            // check stagr references
            entry.senses.stream()
                .filter { it.stagrs != null }
                .flatMap { it.stagrs!!.stream() }
                .forEach { referencedKana ->
                    val brokenReference = !entryKanaStrings.contains(referencedKana)
                    if (brokenReference) {
                        println("Warning: entry ${entry.id} has broken stagt ref $referencedKana")
                    }
                }

            // check antonym references
            entry.senses.stream()
                .filter { it.antonyms != null }
                .flatMap { it.antonyms!!.stream() }
                .flatMap { antonymEntryString -> antonymEntryString.split(antonymInBandDelimiter).stream() }
                .filter { antonymWordOrNum -> antonymWordOrNum.toIntOrNull() == null }
                .forEach { antonymWord ->
                    val match = findWordRef(entries, antonymWord)
                    if (match == null || match.id == entry.id)
                        println("Warning: entry ${entry.id} has an antonym $antonymWord that doesn't match any other entry")
                }
        }
    }

    private fun findWordRef(entries: List<DictEntry>, wordRef: String): DictEntry? {
        return entries.find { entry ->
            entry.kanas.any { it.str == wordRef }
                    ||
                    entry.kanjis?.any { it.str == wordRef } ?: false
        }
    }

    private fun checkSample(entries: List<DictEntry>) {
        val entryCheckExpected = DictEntry(
            1038660,
            listOf(Kanji("空オケ", null, null)),
            listOf(
                Kana("からオケ", null, null, null, null),
                Kana("カラオケ", null, listOf(Priority.ICHI1, Priority.SPEC1), null, true)
            ),
            listOf(
                Sense(
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
                    listOf(Gloss("karaoke", Lang.ENG, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke", Lang.DUT, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoké", Lang.FRE, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("Karaoke (wörtl. leeres Orchester)", Lang.GER, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("караоке", Lang.RUS, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke (petje s posneto spremljavo)", Lang.SLV, null))
                ),
                Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke (cantar música grabada)", Lang.SPA, null), Gloss("karaoke", Lang.SPA, null))
                ), Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke", Lang.SWE, null))
                )

            )
        )
        val entryCheckActual = entries[3445]
        if (entryCheckActual != entryCheckExpected) error("possible parsing error")
    }
}