package com.alun.indexcreator

import org.w3c.dom.Document
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class DTDValidator {
    fun throwIfDtdInvalid(filePath: String) {
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