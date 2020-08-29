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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alun.yaku.R
import com.alun.yaku.fragments.SearchResultsFragment
import com.alun.yaku.models.SearchParams
import kotlinx.android.synthetic.main.activity_search_results.*

class SearchResultsActivity : AppCompatActivity() { // TODO extend FragmentActivity directly instead, I think AppCompatActivity is deprecated?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val isFirstCreation = savedInstanceState == null
        val layoutHasSearchResultsFrame = activity_search_results_frame_layout != null
        if (isFirstCreation && layoutHasSearchResultsFrame) {
            val searchParams = intent.getParcelableExtra<SearchParams>(INTENT_KEY_SEARCH_PARAMS)
            if (searchParams == null) TODO("Handle search params being null?");
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.activity_search_results_frame_layout,
                    SearchResultsFragment.newInstance(searchParams)
                )
                .commit()
        }
        // TODO load saved instance state
    }

    companion object {
        private val INTENT_KEY_SEARCH_PARAMS = "intent_key_search_params"

        @JvmStatic
        fun newInstance(context: Activity, searchParams: SearchParams) {
            val intent = Intent(context, SearchResultsActivity::class.java)
            intent.putExtra(INTENT_KEY_SEARCH_PARAMS, searchParams)
            context.startActivity(intent)
        }
    }
}