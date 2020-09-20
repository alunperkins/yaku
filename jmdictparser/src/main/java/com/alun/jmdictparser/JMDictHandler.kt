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
import com.alun.common.utils.AlphabetDetector.Companion.isKana
import com.alun.jmdictparser.models.GlossAttrs
import com.alun.jmdictparser.models.LoanSourceAttrs
import com.alun.jmdictparser.models.Tag
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class JMDictHandler : DefaultHandler()/*default handler is just a template, all its methods have empty bodies*/ {

    val entries = ArrayList<DictEntry>() // TODO publish these reactively, instead of collecting them all?

    private var entryId: Int? = null
    private var entryKanjis: MutableList<Kanji>? = null
    private var entryKanjiValue: String? = null
    private var entryKanjiInfos: MutableList<KanjiInfo>? = null
    private var entryKanjiPriorities: MutableList<Priority>? = null
    private var entryKanas: MutableList<Kana>? = null
    private var entryKanaValue: String? = null
    private var entryKanaInfos: MutableList<ReadingInfo>? = null
    private var entryKanaPriorities: MutableList<Priority>? = null
    private var entryKanaRestrs: MutableList<String>? = null
    private var entryKanaNoKanji: Boolean? = null
    private var entrySenses: MutableList<Sense>? = null
    private var entrySenseStagks: MutableList<String>? = null
    private var entrySenseStagrs: MutableList<String>? = null
    private var entrySensePoss: MutableList<POS>? = null
    private var entrySenseXrefs: MutableList<Reference>? = null
    private var entrySenseAnts: MutableList<Reference>? = null
    private var entrySenseFields: MutableList<Field>? = null
    private var entrySenseMiscs: MutableList<Misc>? = null
    private var entrySenseInfos: MutableList<String>? = null
    private var entrySenseLsources: MutableList<LoanSource>? = null
    private var entrySenseLsourceAttrLang: Lang? = null
    private var entrySenseLsourceAttrType: LoanType? = null
    private var entrySenseLsourceAttrWasei: Boolean? = null
    private var entrySenseDials: MutableList<Dialect>? = null
    private var entrySenseGlosses: MutableList<Gloss>? = null
    private var entrySenseGlossAttrLang: Lang? = null
    private var entrySenseGlossAttrType: GlossType? = null
    private var cData: String? = null

    private val referenceInBandDelimiter = '・'

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val str = String(ch!!, start, length)
        cData = if (cData == null) str else cData.plus(str)
    }

    override fun startElement(
        uri: String?, // non-null, usu. empty
        localName: String?, // non-null, usu. empty
        qName: String?, // non-null, xml tag name
        attributes: Attributes? // non-null, each attr has uri, localname, qname, type, value. We only want qname, type(usu. "CDATA") and value
    ) {
        if (cData != null) error("cData should be null but is $cData")
        when (Tag.fromStr(qName!!)) {
            Tag.JMDict -> {
            }
            Tag.Entry -> {
            }
            Tag.EntSeq -> {
            }
            Tag.Kanji -> {
                if (entryKanjis == null) entryKanjis = mutableListOf()
            }
            Tag.KanjiValue -> {
            }
            Tag.KanjiInfo -> {
                if (entryKanjiInfos == null) entryKanjiInfos = mutableListOf()
            }
            Tag.KanjiPri -> {
                if (entryKanjiPriorities == null) entryKanjiPriorities = mutableListOf()
            }
            Tag.Kana -> {
                if (entryKanas == null) entryKanas = mutableListOf()
            }
            Tag.KanaValue -> {
            }
            Tag.KanaInfo -> {
                if (entryKanaInfos == null) entryKanaInfos = mutableListOf()
            }
            Tag.KanaPri -> {
                if (entryKanaPriorities == null) entryKanaPriorities = mutableListOf()
            }
            Tag.KanaRestr -> {
                if (entryKanaRestrs == null) entryKanaRestrs = mutableListOf()
            }
            Tag.KanaNoKanji -> {
            }
            Tag.Sense -> {
                if (entrySenses == null) entrySenses = mutableListOf()
            }
            Tag.SenseTagk -> {
                if (entrySenseStagks == null) entrySenseStagks = mutableListOf()
            }
            Tag.SenseTagr -> {
                if (entrySenseStagrs == null) entrySenseStagrs = mutableListOf()
            }
            Tag.SensePOS -> {
                if (entrySensePoss == null) entrySensePoss = mutableListOf()
            }
            Tag.SenseXRef -> {
                if (entrySenseXrefs == null) entrySenseXrefs = mutableListOf()
            }
            Tag.SenseAnt -> {
                if (entrySenseAnts == null) entrySenseAnts = mutableListOf()
            }
            Tag.SenseField -> {
                if (entrySenseFields == null) entrySenseFields = mutableListOf()
            }
            Tag.SenseMisc -> {
                if (entrySenseMiscs == null) entrySenseMiscs = mutableListOf()
            }
            Tag.SenseInfo -> {
                if (entrySenseInfos == null) entrySenseInfos = mutableListOf()
            }
            Tag.SenseLSource -> {
                if (entrySenseLsources == null) entrySenseLsources = mutableListOf()
                if (attributes !== null && attributes.length > 0) {
                    for (i in 0 until attributes.length) {
                        val attrVal = attributes.getValue(i)
                        when (LoanSourceAttrs.fromStr(attributes.getQName(i))) {
                            LoanSourceAttrs.Lang -> entrySenseLsourceAttrLang = Lang.fromStr(attrVal)
                            LoanSourceAttrs.Type -> entrySenseLsourceAttrType = LoanType.fromStr(attrVal)
                            LoanSourceAttrs.Wasei -> entrySenseLsourceAttrWasei =
                                if (attrVal == "y") true else error("unexpected value for $Tag.SenseLSource of $attrVal")
                        }
                    }
                }
            }
            Tag.SenseDialect -> {
                if (entrySenseDials == null) entrySenseDials = mutableListOf()
            }
            Tag.SenseGloss -> {
                if (entrySenseGlosses == null) entrySenseGlosses = mutableListOf()
                if (attributes !== null && attributes.length > 0) {
                    for (i in 0 until attributes.length) {
                        val attrVal = attributes.getValue(i)
                        when (GlossAttrs.fromStr(attributes.getQName(i))) {
                            GlossAttrs.Lang -> entrySenseGlossAttrLang = Lang.fromStr(attrVal)
                            GlossAttrs.Type -> entrySenseGlossAttrType = GlossType.fromStr(attrVal)
                            GlossAttrs.Gend -> error("$GlossAttrs.Gend attributes appears but is not supported because assumed not to appear because does not appear in the copy of JMDict used during development")
                        }
                    }
                }
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        // consume saved information into model objects, then clear that information
        when (Tag.fromStr(qName!!)) {
            Tag.JMDict -> {
            }
            Tag.Entry -> {
                entries.add(DictEntry(entryId!!, entryKanjis, entryKanas!!, entrySenses!!))
                entryId = null
                entryKanjis = null
                entryKanas = null
                entrySenses = null
            }
            Tag.EntSeq -> {
                entryId = cData!!.toInt(10)
//                println("entry $entryId")
                cData = null
            }
            Tag.Kanji -> {
                entryKanjis!!.add(
                    Kanji(
                        entryKanjiValue!!,
                        entryKanjiInfos,
                        entryKanjiPriorities
                    )
                )
                entryKanjiValue = null
                entryKanjiInfos = null
                entryKanjiPriorities = null
            }
            Tag.KanjiValue -> {
                entryKanjiValue = cData!!
                cData = null
            }
            Tag.KanjiInfo -> {
                entryKanjiInfos!!.add(KanjiInfo.fromStr(cData!!))
                cData = null
            }
            Tag.KanjiPri -> {
                entryKanjiPriorities!!.add(Priority.fromStr(cData!!))
                cData = null
            }
            Tag.Kana -> {
                entryKanas!!.add(
                    Kana(
                        entryKanaValue!!,
                        entryKanaInfos,
                        entryKanaPriorities,
                        entryKanaRestrs,
                        entryKanaNoKanji
                    )
                )
                entryKanaValue = null
                entryKanaInfos = null
                entryKanaPriorities = null
                entryKanaRestrs = null
                entryKanaNoKanji = null
            }
            Tag.KanaValue -> {
                entryKanaValue = cData!!
                cData = null
            }
            Tag.KanaInfo -> {
                entryKanaInfos!!.add(ReadingInfo.fromStr(cData!!))
                cData = null
            }
            Tag.KanaPri -> {
                entryKanaPriorities!!.add(Priority.fromStr(cData!!))
                cData = null
            }
            Tag.KanaRestr -> {
                entryKanaRestrs!!.add(cData!!)
                cData = null
            }
            Tag.KanaNoKanji -> {
                entryKanaNoKanji = true
            }
            Tag.Sense -> {
                if (entrySenseGlosses.isNullOrEmpty()) {
                    println("Warning: $entryId has a sense with no glosses, ignoring that sense")
                    /*
                    No-gloss senses are allowed in "entries which are purely for a cross-reference".
                    They occur 6 times, as totally empty sense tags `<sense></sense>` (no glosses and nothing else either).
                    They are not supported.
                    */
                } else {
                    if ((entrySenseStagks != null) && (entrySenseStagrs != null)) {
                        println("Entry $entryId has both stagk(s) and stagr(s)")
                    }
                    entrySenses!!.add(
                        Sense(
                            entrySenseStagks,
                            entrySenseStagrs,
                            entrySensePoss,
                            entrySenseXrefs,
                            entrySenseAnts,
                            entrySenseFields,
                            entrySenseMiscs,
                            entrySenseInfos,
                            entrySenseLsources,
                            entrySenseDials,
                            entrySenseGlosses!!
                        )
                    )
                    entrySenseStagks = null
                    entrySenseStagrs = null
                    entrySensePoss = null
                    entrySenseXrefs = null
                    entrySenseAnts = null
                    entrySenseFields = null
                    entrySenseMiscs = null
                    entrySenseInfos = null
                    entrySenseLsources = null
                    entrySenseDials = null
                    entrySenseGlosses = null
                }
            }
            Tag.SenseTagk -> {
                entrySenseStagks!!.add(cData!!)
                cData = null
            }
            Tag.SenseTagr -> {
                entrySenseStagrs!!.add(cData!!)
                cData = null
            }
            Tag.SensePOS -> {
                entrySensePoss!!.add(POS.fromStr(cData!!))
                cData = null
            }
            Tag.SenseXRef -> {
                entrySenseXrefs!!.add(parseReference(cData!!))
                cData = null
            }
            Tag.SenseAnt -> {
                entrySenseAnts!!.add(parseReference(cData!!))
                cData = null
            }
            Tag.SenseField -> {
                entrySenseFields!!.add(Field.fromStr(cData!!))
                cData = null
            }
            Tag.SenseMisc -> {
                entrySenseMiscs!!.add(Misc.fromStr(cData!!))
                cData = null
            }
            Tag.SenseInfo -> {
                entrySenseInfos!!.add(cData!!)
                cData = null
            }
            Tag.SenseLSource -> {
                entrySenseLsources!!.add(
                    LoanSource(
                        cData,
                        entrySenseLsourceAttrLang!!,
                        entrySenseLsourceAttrType,
                        entrySenseLsourceAttrWasei
                    )
                )
                cData = null
                entrySenseLsourceAttrLang = null
                entrySenseLsourceAttrType = null
                entrySenseLsourceAttrWasei = null
            }
            Tag.SenseDialect -> {
                entrySenseDials!!.add(Dialect.fromStr(cData!!))
                cData = null
            }
            Tag.SenseGloss -> {
                if (cData.isNullOrEmpty())
                    println("Warning: $entryId has a gloss with no text, ignoring that gloss") // occurs because entry 1422200 has a sense `<sense><gloss xml:lang="spa"></gloss></sense>`
                else {
                    entrySenseGlosses!!.add(
                        Gloss(
                            cData!!,
                            entrySenseGlossAttrLang!!,
                            entrySenseGlossAttrType
                        )
                    )
                    cData = null
                    entrySenseGlossAttrLang = null
                    entrySenseGlossAttrType = null
                }
            }
        }
    }

    /**
     * Input is taken directly from the XML and is a string part optionally followed by a "・" delimeter and a sense number reference
     * The string part may be a single string containing "・"s, or may be two strings without dots concatenated into one string with a "・" delimiter
     * i.e.
     * * a kanji string of another entry, possibly containing "・" characters
     * * a kana string of another entry, possibly containing "・" characters
     * * a kanji string of another entry containing no "・"s, a "・" delimeter, and then a kana string of another entry containing no "・"s
     * i.e. "{kanji}" or "{kana}" or "{kanjiContainingNoDots}・{kanaContainingNoDots}"
     */
    private fun parseReference(_str: String): Reference {
        val allParts = _str.split(referenceInBandDelimiter)

        val senseNo = allParts.last().toIntOrNull()

        val stringParts = if (senseNo == null)
            allParts.subList(0, allParts.size)
        else
            allParts.subList(0, allParts.size - 1).ifEmpty { throw Error("Unsupported format in reference") }

        val str = stringParts.joinToString(separator = referenceInBandDelimiter.toString())
        val kanji: String?
        val kana: String?
        if (isKana(str)) {
            kanji = null
            kana = str
        } else if(stringParts.size == 2 && !isKana(stringParts[0]) && isKana(stringParts[1])) {
            kanji = stringParts[0]
            kana = stringParts[1]
        } else {
            kanji = str
            kana = null
        }

        return Reference(kanji, kana, senseNo)
    }
}
