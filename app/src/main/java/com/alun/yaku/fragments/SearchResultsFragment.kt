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
package com.alun.yaku.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alun.yaku.R
import com.alun.yaku.models.Result
import com.alun.yaku.models.SearchResults
import com.alun.yaku.models.WordDetail
import com.alun.yaku.recyclerviewadapters.DictEntryAdapter
import com.alun.yaku.viewmodels.EntrySelectedViewModel
import com.alun.yaku.viewmodels.SearchResultsViewModel
import kotlinx.android.synthetic.main.fragment_search_results.*

class SearchResultsFragment : Fragment() {
    private val searchResultsViewModel: SearchResultsViewModel by activityViewModels()
    private val entrySelectedViewModel: EntrySelectedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_results.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DictEntryAdapter(listOf())
        }

        search_results_status_text.text = resources.getString(R.string.searching)
        searchResultsViewModel.results.observe(viewLifecycleOwner, Observer { searchResults: SearchResults? ->
            if (searchResults == null) return@Observer
            val query = searchResults.query
            when (val result = searchResults.result) {
                is Result.Success -> {
                    val matches = result.data
                    search_results_status_text.text = resources.getQuantityString(
                        R.plurals.number_of_matches_for_query,
                        matches.size,
                        matches.size,
                        query.text
                    )

                    val resultsAdapter = DictEntryAdapter(matches).apply {
                        clickListener = object : DictEntryAdapter.ClickListener {
                            override fun onClick(position: Int) {
                                entrySelectedViewModel.entrySelected.postValue(WordDetail(matches[position]))
                                println(
                                    "SEARCH RESULTS FRAGMENT CLICK LISTENER " + position + "  " + matches[position].toString()
                                )
                            }
                        }
                    }
                    search_results.swapAdapter(resultsAdapter, true)
                }
                is Result.Failure -> {
                    result.throwable.printStackTrace() // TODO remove
                    search_results_status_text.text = resources.getString(
                        R.string.error,
                        result.throwable.localizedMessage
                    ) // TODO review what is best to do, this text may not be helpful to the user
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchResultsFragment()
    }
}
