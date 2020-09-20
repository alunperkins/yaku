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

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alun.common.models.DictEntry
import com.alun.yaku.fragments.SearchFragment
import com.alun.yaku.fragments.SearchResultsFragment
import com.alun.yaku.models.SearchParams
import com.alun.yaku.viewmodels.SearchResultsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recentlyViewedListViewAdapter: DictEntryAdapter
    private lateinit var recentlyViewedListViewManager: LinearLayoutManager
    private val searchResultsViewModel: SearchResultsViewModel by viewModels()

    val words: List<DictEntry> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFirstCreation = savedInstanceState == null // TODO check that this can be relied on
        if (isFirstCreation) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_fragment_holder, SearchFragment.newInstance())
                .commit()
        }

        if (savedInstanceState == null) {
            println("==== MainActivity : onCreate : savedInstanceState is null")
        } else {
            println(
                "MainActivity " +
                        ": onCreate " +
                        ": savedInstanceState has keys " + savedInstanceState.keySet().joinToString(separator = ", ")
            )
        }

        searchResultsViewModel.executedSearch.observe(this, Observer { executedSearch: SearchParams? ->
            println("executedSearch: $executedSearch")
            if (executedSearch != null) {
                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.activity_main_fragment_holder, SearchResultsFragment.newInstance())
                    .commit()
                // TODO conditionally put the fragment in the right of a dual-pane layout depending on if right-pane fragment holder is in the xml
            }
        })

        initRecentlyViewedList()
    }

    private fun initRecentlyViewedList() { // TODO recently viewed list should be a separate fragment (supported by some persistent storage implementation
        recentlyViewedListViewManager = LinearLayoutManager(this)

        recentlyViewedListViewAdapter = DictEntryAdapter(words).apply {
            clickListener = object : DictEntryAdapter.ClickListener {
                override fun onClick(position: Int) {
                    println(
                        "MAIN ACTIVITY CLICK LISTENER " + position + "  " + words.get(position)
                            .toString()
                    )
//                    search_src_text.setText(words.get(position).reading)
                }
            }
        }

        list_recently_viewed.apply {
            layoutManager = recentlyViewedListViewManager
            adapter = recentlyViewedListViewAdapter
            setHasFixedSize(true)
        }
    }
}