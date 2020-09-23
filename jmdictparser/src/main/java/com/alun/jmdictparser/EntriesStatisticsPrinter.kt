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
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.math.roundToInt
import kotlin.streams.toList

class EntriesStatisticsPrinter {
    fun printStatistics(entries: List<DictEntry>) {
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
                .map { it.glosses[0].lang } // the language of each sense (assuming glosses of a single sense are homogeneous in lang)
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

    private fun <T : Comparable<T>?> histogram(
        entries: List<DictEntry>,
        getKeys: (DictEntry) -> List<T>
    ) {
        val counts = entries.stream()
            .flatMap { entry: DictEntry -> getKeys(entry).stream() }
            .collect(Collectors.groupingBy(Function { key: T -> key }, Collectors.counting()))
        val total = counts.values.sum()
        for (key in counts.keys.stream().sorted().toList())/* .sortedBy { it.toString() })*/ {
            val occurrences = counts[key]!!
            println(key.toString() + "," + occurrences + "," + (100 * occurrences.toDouble() / total.toDouble()).roundToInt() + "%")
        }
    }
}