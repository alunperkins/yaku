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
package com.alun.yaku

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alun.common.models.DictEntry
import kotlinx.coroutines.launch

class SearchResultsViewModel() : ViewModel() {
    val results = MutableLiveData<Result<List<DictEntry>>>(null)

    fun search(context: Context?, params: SearchParams) {
        val t1 = System.currentTimeMillis()
        val searchService: SearchService = SearchServiceImplLucene(context)
        viewModelScope.launch {
            results.postValue(
                try {
                    Result.Success(searchService.getResults(params))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            )
            println("==== SearchResultsViewModel::search searched in " + (System.currentTimeMillis() - t1) + " wall clock milliseconds")
        }

    }
}