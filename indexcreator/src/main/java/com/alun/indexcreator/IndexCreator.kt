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

import com.alun.common.models.DictEntry
import com.alun.common.models.Lang
import com.alun.common.models.LuceneFields.Companion.getFieldName
import com.alun.common.models.LuceneFields.Companion.getFieldNameEntry
import com.alun.common.models.LuceneFields.Companion.getFieldNameKana
import com.alun.common.models.LuceneFields.Companion.getFieldNameKanji
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.*
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.Directory
import org.apache.lucene.store.MMapDirectory
import org.apache.lucene.util.Version
import java.io.File

class IndexCreator {
    fun run(entries: List<DictEntry>, outputPath: String) {
        val dir = MMapDirectory(File(outputPath));
        val analyzer = PerFieldAnalyzerWrapper(
            StandardAnalyzer(Version.LUCENE_47),
            mapOf(
                getFieldNameKana() to JapaneseAnalyzer(Version.LUCENE_47),
                getFieldNameKanji() to JapaneseAnalyzer(Version.LUCENE_47)
            )
        )

        val indexWriterConfig = IndexWriterConfig(Version.LUCENE_47, analyzer)
        val writer = IndexWriter(dir, indexWriterConfig)

        val json = Json(JsonConfiguration.Stable)
        val serializer = DictEntry.serializer()
        entries.forEach { entry ->
            val document = Document()
            entry.kanas.forEach { kana ->
                document.add(TextField(getFieldNameKana(), kana.str, Field.Store.NO))
            }
            entry.kanjis?.forEach { kanji ->
                document.add(TextField(getFieldNameKanji(), kanji.str, Field.Store.NO))
            }
            entry.senses.forEach { sense ->
                sense.glosses.forEach { gloss ->
                    document.add(TextField(getFieldName(sense.lang), gloss.str, Field.Store.NO))
                }
            }
            document.add(TextField(getFieldNameEntry(), json.stringify(serializer, entry), Field.Store.YES))
            writer.addDocument(document)
        }

        writer.close()

        return
    }
}