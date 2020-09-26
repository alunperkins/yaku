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

import org.w3c.dom.Document
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class DTDValidator {
    fun requireDTDValid(filePath: String) {
        val domFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance().apply {
            isValidating = true
        }

        val builder: DocumentBuilder = domFactory.newDocumentBuilder().apply {
            setErrorHandler(object : ErrorHandler {
                override fun error(exception: SAXParseException) {
                    // do something more useful in each of these handlers
                    exception.printStackTrace()
                }

                override fun fatalError(exception: SAXParseException) {
                    exception.printStackTrace()
                }

                override fun warning(exception: SAXParseException) {
                    exception.printStackTrace()
                }
            })
        }

        val doc: Document = builder.parse(filePath) // throws with informative message if DTD is invalid
        println("DTD valid")
        return;
    }
}