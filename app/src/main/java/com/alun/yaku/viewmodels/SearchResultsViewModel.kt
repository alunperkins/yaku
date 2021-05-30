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
import com.alun.yaku.models.Result
import com.alun.yaku.models.SearchParams
import com.alun.yaku.models.SearchResults
import com.alun.yaku.services.SearchService
import com.alun.yaku.services.SearchServiceImplLucene
import kotlinx.coroutines.launch

class SearchResultsViewModel() : ViewModel() {
    val executedSearch = MutableLiveData<SearchParams?>(null)
    val results = MutableLiveData<SearchResults?>(null) // TODO can it be made non-nullable?

    /**
     * gets search results, filtering to the target lang only
     */
    fun search(context: Context?, params: SearchParams) {
        executedSearch.postValue(params) // TODO should we results.postValue(null) at this point, since otherwise results will be stale w.r.t. executedSearch?
        val searchService: SearchService = SearchServiceImplLucene(context)
        viewModelScope.launch {
            val searchResult = try {
                val matchesForUsersLanguage = searchService.getResults(params)
                    .map { discardSensesInOtherLangs(it, params.lang) }
                    .filter { entry -> entry.senses.isNotEmpty() }
                Result.Success(matchesForUsersLanguage)
            } catch (throwable: Throwable) {
                Result.Failure(throwable)
            }
            results.postValue(SearchResults(params, searchResult))
        }

    }


    private fun discardSensesInOtherLangs(entry: DictEntry, lang: Lang): DictEntry { // TODO consider moving to utils
        return DictEntry(
            entry.id,
            entry.kanjis,
            entry.kanas,
            entry.senses.filter { it.lang == lang }
        )
    }
}