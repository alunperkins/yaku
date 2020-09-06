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
package com.alun.yaku.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alun.common.models.DictEntry
import com.alun.yaku.DictEntryAdapter
import com.alun.yaku.R
import com.alun.yaku.fragments.SearchFragment
import com.alun.yaku.models.SearchParams
import com.alun.yaku.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recentlyViewedListViewAdapter: DictEntryAdapter
    private lateinit var recentlyViewedListViewManager: LinearLayoutManager
    private val searchViewModel: SearchViewModel by viewModels()

    val words: List<DictEntry> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFirstCreation = savedInstanceState == null
        val layoutHasSearchFragmentFrame = activity_main_search_frame_layout != null
        if (isFirstCreation && layoutHasSearchFragmentFrame) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_search_frame_layout, SearchFragment.newInstance())
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

        searchViewModel.executedSearch.observe(this, Observer { executedSearch: SearchParams? ->
            if (executedSearch != null) {
                // TODO conditionally start SearchResultsActivity or replace a fragment depending on if search results fragment holder is in the xml
                SearchResultsActivity.newInstance(this, executedSearch)
            }
        })

        initRecentlyViewedList()
    }

    private fun initRecentlyViewedList() {
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