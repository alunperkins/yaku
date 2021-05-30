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
package com.alun.yaku.services

import android.content.Context
import com.alun.common.models.DictEntry
import com.alun.common.models.LuceneFields.Companion.getFieldName
import com.alun.common.models.LuceneFields.Companion.getFieldNameEntry
import com.alun.common.models.LuceneFields.Companion.getFieldNameKana
import com.alun.common.models.LuceneFields.Companion.getFieldNameKanji
import com.alun.common.utils.AlphabetDetector
import com.alun.yaku.models.SearchMode
import com.alun.yaku.models.SearchParams
import com.alun.yaku.models.SearchTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.Term
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory

class SearchServiceImplLucene(context: Context?) : SearchService {
    // TODO handle org.apache.lucene.index.IndexNotFoundException
    private val searcher = context?.let { IndexSearcher(DirectoryReader.open(getDirectory(it))) }

    private val json = Json(JsonConfiguration.Stable)
    private val serializer = DictEntry.serializer()

    override suspend fun getResults(params: SearchParams): List<DictEntry> {
        return withContext(Dispatchers.IO) {
            if (searcher == null) TODO("handle case where searcher is null because context was null")
            when (params.searchTarget) {
                SearchTarget.WORDS -> {
                    val targetFieldName = when (params.searchMode) {
                        SearchMode.FROM_ENGLISH -> getFieldName(params.lang)
                        SearchMode.FROM_JAPANESE -> {
                            if (AlphabetDetector.isKana(params.text)) getFieldNameKana()
                            else getFieldNameKanji()
                        }
                        SearchMode.FROM_JAPANESE_DEINFLECTED -> TODO("Deinflection not implemented")
                    }

                    val q: Query = TermQuery(Term(targetFieldName, params.text))
                    val docs: TopDocs = searcher.search(q, 30)
                    val hits = docs.scoreDocs

                    hits.map { hit ->
                        val doc = searcher.doc(hit.doc)
                        val serialized = doc.getField(getFieldNameEntry()).stringValue()

                        /*
                        TODO
                          this could throw if the code's data format is different
                          between the one in the app and the one used to generate the index
                        */
                        val deserialized = json.parse(serializer, serialized)

                        deserialized
                    }
                }
                SearchTarget.EXAMPLES -> TODO("Searching examples not implemented")
            }
        }
    }

    private fun getDirectory(context: Context): Directory {
        return FSDirectory.open(context.getExternalFilesDir("index_jmdict"))
    }
}