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
import javax.xml.parsers.SAXParserFactory

/**
 * Read the XML into Kotlin objects
 *
 * requires environment variable '-DentityExpansionLimit=1000000' (or another large no., but default val is too small)
 *
 * designed to work with my copy of JMdict
 * retrieved Sat 18 Jul 10:20:12 BST 2020
 * Rev 1.09
 * md5sum 31f5a9b3bf2923619a1900bf88719ef5  JMdict.gz
 * sha256sum c72c29e3320a2dbed157eb2c6251b0a3e3a8fefff9f8ff9d4876808eff505165  JMdict.gz
 *
 * @param args path to JMdict.xml
 */
class JMDictParser {
    fun run(pathJmDictXml: String): List<DictEntry> {
        DTDValidator().throwIfDtdInvalid(pathJmDictXml)

        val handler = JMDictHandler()
        SAXParserFactory.newInstance().newSAXParser().parse(pathJmDictXml, handler)

        val entries = handler.entries

        val validator = EntriesValidator()
        validator.checkSample(entries)
        validator.validateThatAllNonNullMembersAreNonEmpty(entries)
        validator.checkReferentialIntegrity(entries)

        return entries
    }
}