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
package com.alun.yaku.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alun.common.models.DictEntry
import com.alun.common.models.Lang
import com.alun.yaku.SearchService
import com.alun.yaku.SearchServiceImplLucene
import com.alun.yaku.models.Result
import com.alun.yaku.models.SearchParams
import kotlinx.coroutines.launch

class SearchResultsViewModel() : ViewModel() {
    val results = MutableLiveData<Result<List<DictEntry>>?>(null)

    val targetLang = Lang.ENG // TODO use user-selected language, do not hard-code to English

    /**
     * gets search results, filtering to the target lang only
     */
    fun search(context: Context?, params: SearchParams) {
        val searchService: SearchService = SearchServiceImplLucene(context)
        viewModelScope.launch {
            results.postValue(
                try {
                    val matchesForUsersLanguage = searchInTargetLang(searchService, params)
                    Result.Success(matchesForUsersLanguage)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            )
        }

    }

    private suspend fun searchInTargetLang(
        searchService: SearchService,
        params: SearchParams
    ): List<DictEntry> {
        val matches = searchService.getResults(params)
        val matchesTrimmedToTargetLang = matches
            .map { entry ->
                if (entry.senses.any { sense -> sense.glosses.isNotEmpty() && sense.glosses.any { gloss -> gloss.lang != sense.glosses[0].lang } }) {
                    println("WARNING: Broken assumption: some sense(s) have glosses heterogeneous in language! Will cause results display to display broken info.")
                }
                val senses =
                    entry.senses.filter { sense -> sense.glosses.any { gloss -> gloss.lang == targetLang } } // ASSUMES that all glosses of a sense have the same lang!
                DictEntry(entry.id, entry.kanjis, entry.kanas, senses)
            }
            .filter { entry -> entry.senses.isNotEmpty() }
        return matchesTrimmedToTargetLang
    }

}