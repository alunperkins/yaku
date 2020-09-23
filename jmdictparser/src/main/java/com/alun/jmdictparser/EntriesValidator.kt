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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.math.roundToInt

fun DictEntry.validateThatAllNonNullMembersAreNonEmpty() {
    if (kanjis != null && kanjis!!.isEmpty()) error("non-null member should be non-empty")
    if (kanas.isEmpty()) error("non-null member should be non-empty")
    if (senses.isEmpty()) error("non-null member should be non-empty")
}

fun Gloss.validateThatAllNonNullMembersAreNonEmpty() {
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

fun Sense.validateThatAllNonNullMembersAreNonEmpty() {
    if (stagks != null && stagks!!.isEmpty()) error("non-null member should be non-empty")
    if (stagrs != null && stagrs!!.isEmpty()) error("non-null member should be non-empty")
    if (pos != null && pos!!.isEmpty()) error("non-null member should be non-empty")
    if (xrefs != null && xrefs!!.isEmpty()) error("non-null member should be non-empty")
    if (antonyms != null && antonyms!!.isEmpty()) error("non-null member should be non-empty")
    if (fields != null && fields!!.isEmpty()) error("non-null member should be non-empty")
    if (miscs != null && miscs!!.isEmpty()) error("non-null member should be non-empty")
    if (infos != null && infos!!.isEmpty()) error("non-null member should be non-empty")
    if (loanSource != null && loanSource!!.isEmpty()) error("non-null member should be non-empty")
    if (dialect != null && dialect!!.isEmpty()) error("non-null member should be non-empty")
    if (glosses.isEmpty()) error("non-null member should be non-empty")
}

class EntriesValidator {
    fun validateThatAllNonNullMembersAreNonEmpty(entries: List<DictEntry>) {
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

    fun checkPosAvailabilityByLanguage(entries: List<DictEntry>) {
        // verify that the first sense of an entry always has POS info
        if (entries.all { it.senses[0].pos != null }) println("POS: POS info is always available")
        else error("POS info is NOT ALWAYS AVAILABLE!")

        print("Do all the entries have all english senses before any foreign sense? Search for exceptions: ")
        val entriesWithSensesNotStrictlyEngFirst = entries.filter { entry ->
            var firstNonEngHasAppeared = false
            for (sens in entry.senses) {
                if (sens.glosses[0].lang == Lang.ENG) {
                    if (firstNonEngHasAppeared) return@filter true
                } else firstNonEngHasAppeared = true
            }
            return@filter false
        }
        println("There are ${entriesWithSensesNotStrictlyEngFirst.size} entries where an Eng sense appears after a non-eng one")

        print("Do non-Eng senses EVER have POS explicitly specified? ")
        val sensesWhereNonEngHasAPos =
            entries.filter { entry -> entry.senses.any { sense -> sense.glosses[0].lang != Lang.ENG && sense.pos != null } }
        println("There are ${sensesWhereNonEngHasAPos.size} entries where a non-Eng sense has a pos")

        if (entriesWithSensesNotStrictlyEngFirst.isEmpty() && sensesWhereNonEngHasAPos.isEmpty())
            println("POS does not seem to be supported on languages other than English!")
        else error("Pos configuration assumption broken")

        // how common is it that a sense has its POS implied from a "carried-forward" earlier one?
        val noOfEngSensesWithPos =
            entries.map { entry -> entry.senses.filter { it.glosses[0].lang == Lang.ENG }.count { it.pos != null } }
                .sum()
        val noOfEngSensesWithoutPos =
            entries.map { entry -> entry.senses.filter { it.glosses[0].lang == Lang.ENG }.count { it.pos == null } }
                .sum()
        println(
            "$noOfEngSensesWithPos English senses have a pos, " +
                    "and $noOfEngSensesWithoutPos English senses don't" +
                    "(${percent(noOfEngSensesWithoutPos, noOfEngSensesWithPos + noOfEngSensesWithoutPos)}%)"
        )
    }

    fun checkReferentialIntegrity(entries: List<DictEntry>) {
        entries.forEach { checkEntryInternalReferenceIntegrity(it) }

        checkAntonymReferences(entries) // (SLOW)
        checkCrossReferences(entries) // (SLOW)
    }

    private fun checkEntryInternalReferenceIntegrity(entry: DictEntry) {
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

    private fun checkAntonymReferences(entries: List<DictEntry>) {
        checkReferences(entries, { s: Sense -> s.antonyms }, "antonym")
    }

    private fun checkCrossReferences(entries: List<DictEntry>) {
        checkReferences(entries, { s: Sense -> s.xrefs }, "xRef")
    }

    private fun checkReferences(
        entries: List<DictEntry>,
        attributeGetter: (Sense) -> List<Reference>?,
        attrName: String
    ) {
        println("======== $attrName checking")
        val counts: TreeMap<Int, Long> = TreeMap()
        runBlocking {
            entries.forEach { entry ->
                entry.senses
                    .filter { sense -> attributeGetter(sense) != null }
                    .flatMap { sense -> attributeGetter(sense)!! }
                    .map { ref ->
                        async(Dispatchers.Default) {
                            val matches = searchWordRef(entries, ref).filter { it.id != entry.id }
                            if (matches.isEmpty())
                                error(
                                    "Entry ${printEntry(entry)} has an $attrName $ref that " +
                                            "doesn't match any other entry"
                                )
//                            if (matches.size > 1)
//                                println(
//                                    "Warning: entry ${printEntry(entry)} has an $attrName $ref that " +
//                                            "matches multiple (${matches.size}) other entries ${printEntries(matches)}"
//                                )
                            val noOfMatches = matches.size
                            counts.put(noOfMatches, (counts[noOfMatches] ?: 0L) + 1)
                        }
                    }
                    .forEach { it.await() }
            }
        }
        println("==== $attrName reference hits counts")
        val total = counts.values.stream().reduce(0L) { a, b -> a + b }
        for (key in counts.keys) {
            val occurrences = counts[key]!!
            println("$key,$occurrences,${percent(occurrences, total)}%")
        }
    }

    private fun searchWordRef(entries: List<DictEntry>, ref: Reference): List<DictEntry> {
        val allMatches = entries.filter { entry ->
            val kanjiMatch = if (ref.kanji == null) true else (entry.kanjis?.any { it.str == ref.kanji } ?: false)
            val kanaMatch = if (ref.kana == null) true else entry.kanas.any { it.str == ref.kana }
            kanjiMatch && kanaMatch
        }
        val nullKanjiMatches = allMatches.filter { it.kanjis == null }
        if (ref.kanji == null && nullKanjiMatches.isEmpty() && allMatches.size > 1)
            println(
                "Warning: Ref $ref has null kanji but it has multiple (${allMatches.size}) matches, " +
                        "all of which have kanji: ${printEntries(allMatches)}"
            )
        return if (ref.kanji == null && nullKanjiMatches.isNotEmpty()) nullKanjiMatches else allMatches
    }

    private fun printEntries(entries: List<DictEntry>): String {
        return entries.map { printEntry(it) }.joinToString(separator = ", ")
    }

    private fun printEntry(entry: DictEntry): String {
        return listOfNotNull(
            entry.kanjis?.joinToString(prefix = "Kanjis:", separator = ",") { it.str },
            entry.kanas.joinToString(prefix = "Kanas:", separator = ",") { it.str },
            entry.senses.filter { it.glosses.any { gloss -> gloss.lang == Lang.ENG } }.map { it.glosses }
                .flatten().joinToString(prefix = "Eng:", separator = ",") { it.str }
        ).joinToString(prefix = "(", separator = ", ", postfix = ")")
    }

    fun checkSample(entries: List<DictEntry>) {
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
                    listOf(
                        Gloss("karaoke (cantar música grabada)", Lang.SPA, null),
                        Gloss("karaoke", Lang.SPA, null)
                    )
                ), Sense(
                    null, null, null, null, null, null, null, null, null, null,
                    listOf(Gloss("karaoke", Lang.SWE, null))
                )

            )
        )
        val entryCheckActual = entries[3445]
        if (entryCheckActual != entryCheckExpected) error("possible parsing error")
    }

    private fun percent(numerator: Number, denominator: Number): Int {
        return (100 * numerator.toDouble() / denominator.toDouble()).roundToInt()
    }
}
