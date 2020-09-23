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

import com.alun.common.models.DictEntry
import com.alun.common.models.Lang
import com.alun.common.models.Reference
import com.alun.common.models.Sense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.roundToInt
import kotlin.streams.toList

class EntriesStatisticsPrinter {
    fun printVariousStatistics(entries: List<DictEntry>) {
        println("================ statistics printouts ================")
        println("Number of entries: ${entries.size}")

        println("======= KANJIS PER ENTRY =======")
        histogram(entries) { listOf(Integer.toUnsignedLong(it.kanjis?.size ?: 0)) }

        println("======= KANAS PER ENTRY =======")
        histogram(entries) { listOf(Integer.toUnsignedLong(it.kanas.size)) }

        println("======= ENGLISH SENSES PER ENTRY =======")
        histogram(entries) { entry ->
            listOf(entry.senses.filter { sense -> sense.glosses.any { it.lang == Lang.ENG } }.count())
        }

        println("======= (NON-EMPTY) ENGLISH GLOSSES PER SENSE =======")
        histogram(entries) { entry ->
            entry.senses.map { sense ->
                sense.glosses
                    .filter { it.lang === Lang.ENG }
                    .filter { it.str.isNotEmpty() }
                    .count()
            }
        }

        println("======= TOTAL ENGLISH GLOSSES PER ENTRY =======")
        histogram(entries) { entry ->
            listOf(
                entry.senses
                    .map { sense -> sense.glosses.filter { it.lang === Lang.ENG }.count() }
                    .sum()
            )
        }

        println("======= IS THERE ANY SENSE THAT HAS GLOSSES OF MULTIPLE LANGUAGES? If not it would make more sense to put language at the Sense level =======")
        var thereIsASenseWithGlossesInMultipleLanguages = false
        entries.flatMap { entry -> entry.senses }
            .forEach { sense ->
                val glosses = sense.glosses
                if (glosses.isEmpty()) error("Size zero gloss - should never happen!")
                val lang = glosses[0].lang
                if (glosses.any { it.lang != lang }) {
                    thereIsASenseWithGlossesInMultipleLanguages = true
                    println("Heterogeneous language sense found")
                }
            }
        if (thereIsASenseWithGlossesInMultipleLanguages) error("Heterogeneous language sense(s) are present")
        else println("No, in the copy of JMDict I have there is no word with a sense having glosses of different languages, so yes it would make more sense to put language at the sense level")

        println("======= TRANSLATED JAPANESE WORDS PER LANGUAGE - would probably have to be above 40,000 or so to be useful as a dictionary for that language =======")
        histogram(entries) { entry ->
            entry.senses
                .map { it.glosses[0].lang } // the language of each sense (ASSUMING from prev analysis that glosses of a single sense are homogeneous in lang)
                .toSet() // remove duplicates to get a list of languages supported by this entry
                .toList()
        }

        println("======= SENSES WITH A POS PER ENTRY =======")
        histogram(entries) { entry -> listOf(entry.senses.filter { it.pos != null }.count()) }

        println("======= SENSES WITHOUT A POS PER ENTRY =======")
        histogram(entries) { entry -> listOf(entry.senses.filter { it.pos == null }.count()) }

        println("======= ENG SENSES WITH A POS PER ENTRY =======")
        histogram(entries) { entry ->
            listOf(entry.senses
                .filter { it.glosses[0].lang === Lang.ENG }
                .filter { it.pos != null }
                .count()
            )
        }

        println("======= ENG SENSES WITHOUT A POS PER ENTRY =======")
        histogram(entries) { entry: DictEntry ->
            listOf(entry.senses
                .filter { it.glosses[0].lang === Lang.ENG }
                .filter { it.pos == null }
                .count()
            )
        }
    }

    fun analyzeHowReRestrIsUsed(entries: List<DictEntry>) {
        println("======= re_restr use =======")
        val noOfEntriesWithDisjointReRestrCombinations = AtomicInteger(0)
        val noOfEntriesWithPartiallyOverlappingReRestrCombinations = AtomicInteger(0)
        for (entry in entries) {
            var disjoint = true
            val reRestrCombinations = entry.kanas
                .filter { it.onlyForKanjis != null }
                .map { HashSet(it.onlyForKanjis!!) }
            if (reRestrCombinations.isEmpty()) continue
            outerLoop@ for (i in 0 until reRestrCombinations.size - 1) {
                val a = reRestrCombinations[i]
                for (j in i + 1 until reRestrCombinations.size) {
                    val b = reRestrCombinations[j]
                    if (a != b && a.stream().anyMatch { o: String? -> b.contains(o) }) {
                        disjoint = false
//                        println("Warning: entry ${entry.id} has kanas with re_restr tags that overlap partially but not completely")
                        break@outerLoop
                    }
                }
            }
            if (disjoint) noOfEntriesWithDisjointReRestrCombinations.getAndIncrement()
            else noOfEntriesWithPartiallyOverlappingReRestrCombinations.getAndIncrement()
        }
        println(
            "There are " + noOfEntriesWithDisjointReRestrCombinations + " entries with re_restr combinations that are disjoint, " +
                    "and " + noOfEntriesWithPartiallyOverlappingReRestrCombinations + " entries with re_restr combinations that are partially overlapping"
        )
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

    fun checkAntonymReferences(entries: List<DictEntry>) {
        checkReferences(entries, { s: Sense -> s.antonyms }, "antonym")
    }

    fun checkCrossReferences(entries: List<DictEntry>) {
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
                            val matches = lookupWordRef(entries, ref).filter { it.id != entry.id }
                            if (matches.isEmpty())
                                error(
                                    "Entry ${entryString(entry)} has an $attrName $ref that " +
                                            "doesn't match any other entry"
                                )
//                            comment the following println warning because it happens just way too much
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
        val total = counts.values.sum()
        for (key in counts.keys) {
            val occurrences = counts[key]!!
            println("$key,$occurrences,${percent(occurrences, total)}%")
        }
    }

    private fun lookupWordRef(entries: List<DictEntry>, ref: Reference): List<DictEntry> {
        val allMatches = entries.filter { entry ->
            val kanjiMatch = ref.kanji == null || (entry.kanjis?.any { it.str == ref.kanji } ?: false)
            val kanaMatch = ref.kana == null || entry.kanas.any { it.str == ref.kana }
            kanjiMatch && kanaMatch
        }
        val nullKanjiMatches = allMatches.filter { it.kanjis == null }
        if (ref.kanji == null && nullKanjiMatches.isEmpty() && allMatches.size > 1)
            println(
                "Warning: Ref $ref has null kanji but it has multiple (${allMatches.size}) matches, " +
                        "all of which have kanji: ${entriesString(allMatches)}"
            )
        return if (ref.kanji == null && nullKanjiMatches.isNotEmpty()) nullKanjiMatches else allMatches
    }

    private fun entriesString(entries: List<DictEntry>): String {
        return entries.joinToString(separator = ", ") { entryString(it) }
    }

    private fun entryString(entry: DictEntry): String {
        return listOfNotNull(
            entry.kanjis?.joinToString(prefix = "Kanjis:", separator = ",") { it.str },
            entry.kanas.joinToString(prefix = "Kanas:", separator = ",") { it.str },
            entry.senses
                .filter { it.glosses.any { gloss -> gloss.lang == Lang.ENG } }
                .flatMap { it.glosses }
                .joinToString(prefix = "Eng:", separator = ",") { it.str }
        ).joinToString(prefix = "(", separator = ", ", postfix = ")")
    }

    /**
     * print frequency counts for each key
     *
     * for each entry calculate its key(s) and keep a tally of how many times that key occurs
     * output should be csv-compatible, suitable for copy-pasting into a spreadsheet is you want to make a graph
     */
    private fun <T : Comparable<T>?> histogram(
        entries: List<DictEntry>,
        getKeys: (DictEntry) -> List<T>
    ) {
        val counts: Map<T, Long> = entries.stream()
            .flatMap { getKeys(it).stream() }
            .collect(Collectors.groupingBy(Function { key: T  -> key }, Collectors.counting()))

        val total = counts.values.sum()

        for (key in counts.keys.stream().sorted().toList()) {
            val occurrences = counts[key]!!
            println("${key.toString()},$occurrences,${percent(occurrences, total)}%")
        }
    }

    private fun percent(numerator: Number, denominator: Number): Int {
        return (100 * numerator.toDouble() / denominator.toDouble()).roundToInt()
    }
}