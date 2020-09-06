package com.alun.indexcreator

import com.alun.common.models.DictEntry
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.MMapDirectory
import org.apache.lucene.util.Version
import java.io.File

class IndexCreator {
    fun run(entries: List<DictEntry>, outputPath: String) {
        val dir = MMapDirectory(File(outputPath));
        val analyzer = StandardAnalyzer(Version.LUCENE_47);

        val indexWriterConfig = IndexWriterConfig(Version.LUCENE_47, analyzer)
        val writer = IndexWriter(dir, indexWriterConfig)

        val json = Json(JsonConfiguration.Stable)
        val serializer = DictEntry.serializer()
        entries.forEach { entry ->
            val document = Document()
            entry.kanas.forEach { kana ->
                document.add(TextField("kana", kana.str, Field.Store.NO))
            }
            entry.kanjis?.forEach { kana ->
                document.add(TextField("kanji", kana.str, Field.Store.NO))
            }
            entry.senses.forEach { sense ->
                sense.glosses.forEach { gloss ->
                    document.add(TextField(gloss.lang.threeLetterCode, gloss.str, Field.Store.NO))
                }
            }
            document.add(TextField("entry", json.stringify(serializer, entry), Field.Store.YES))
            writer.addDocument(document)
        }

        writer.close()

        return
    }
}