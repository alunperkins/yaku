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
import com.alun.jmdictparser.models.SenseRaw

class DictionaryProvider {
    private val languagesWithPosSupport: List<Lang> = listOf(Lang.ENG)

    fun run(pathJmDictXml: String): List<DictEntry> {
        val entriesInXmlTypes: List<DictEntryRaw> = JMDictParser().run(pathJmDictXml)
        return convertToOurModelTypes(entriesInXmlTypes)
    }

    /*public only for testing*/ fun convertToOurModelTypes(entriesRaw: List<DictEntryRaw>): List<DictEntry> {
        return entriesRaw.map { entryRaw ->
            val inputSenses = entryRaw.senses // senses are assumed to be grouped by language
            assertLangsAreInGroups(inputSenses)
            val outputSenses = mutableListOf<Sense>()

            var prevOutputSense: Sense? = null
            var poss: List<POS>?
            var miscs: List<Misc>?

            for ((index, inputSense) in inputSenses.withIndex()) {
                val lang = getLang(inputSense)

                val firstOfNewLangGroup = index == 0 || lang != getLang(inputSenses[index - 1])
                poss = if (firstOfNewLangGroup || !inputSense.pos.isNullOrEmpty()) inputSense.pos
                else prevOutputSense!!.pos
                miscs = if (firstOfNewLangGroup || !inputSense.miscs.isNullOrEmpty()) inputSense.miscs
                else prevOutputSense!!.miscs

                val posSupported = languagesWithPosSupport.contains(lang)
                if (posSupported && poss == null) error("Assumption broken: found no POS info for a sense with supposedly pos-supported language ${lang}")
                if (!posSupported && poss != null) error("Assumption broken: found POS info for a sense with supposedly non-pos-supported language ${lang}")

                val outputSense = Sense(
                    inputSense.stagks,
                    inputSense.stagrs,
                    lang,
                    poss,
                    inputSense.xrefs,
                    inputSense.antonyms,
                    inputSense.fields,
                    miscs,
                    inputSense.infos,
                    inputSense.loanSource,
                    inputSense.dialect,
                    inputSense.glosses.map { Gloss(it.str, it.type) }
                )
                outputSenses.add(outputSense)

                prevOutputSense = outputSense
            }
            DictEntry(
                entryRaw.id,
                entryRaw.kanjis,
                entryRaw.kanas,
                outputSenses
            )
        }
    }

    private fun assertLangsAreInGroups (senses: List<SenseRaw>) {
        val langsAlreadySeen = mutableSetOf<Lang>()
        var prevLang: Lang?  = null
        for (sense in senses) {
            val lang = getLang(sense)
            val isNewLangGroup = lang != prevLang
            if (isNewLangGroup && langsAlreadySeen.contains(lang)) error("Assumption broken: senses are assumed to be grouped by language")

            langsAlreadySeen.add(lang)

            prevLang = lang
        }
    }

    private fun getLang(s: SenseRaw): Lang {
        val lang = s.glosses[0].lang
        if (s.glosses.any { it.lang != lang }) error("Assumption broken: glosses heterogenous in lang")
        return lang
    }
}