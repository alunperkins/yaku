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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        search_btn_from_english.setOnClickListener { view -> onClickSearchFromEnglish(view) }
        search_btn_from_japanese.setOnClickListener { view -> onClickSearchFromJapanese(view) }
        search_btn_from_japanese_definflecting.setOnClickListener { view ->
            onClickSearchFromJapaneseDeinflecting(
                view
            )
        }
        search_src_text_clear.setOnClickListener { view -> onClickClearSearchString(view) }
    }

    fun onClickClearSearchString(view: View) {
        search_src_text.setText("")
    }

    fun onClickSearchFromEnglish(view: View) {
        search(SearchMode.FROM_ENGLISH)
    }

    fun onClickSearchFromJapanese(view: View) {
        search(SearchMode.FROM_JAPANESE)
    }

    fun onClickSearchFromJapaneseDeinflecting(view: View) {
        search(SearchMode.FROM_JAPANESE_DEINFLECTED)
    }

    private fun search(searchMode: SearchMode) {
        val searchString = search_src_text.text.toString().trim()
        if (searchString.isBlank()) {
            return;
        }
        val searchParams = SearchParams(
            searchString,
            searchMode,
            getMatchModeSelection(),
            getSearchTarget()
        )

        searchViewModel.search.postValue(searchParams)
    }

    fun getMatchModeSelection(): MatchMode {
        return when (radio_match_mode.checkedRadioButtonId) {
            radio_match_mode_any.id -> MatchMode.ANY
            radio_match_mode_exact.id -> MatchMode.EXACT
            radio_match_mode_starts_with.id -> MatchMode.STARTS_WITH
            radio_match_mode_ends_with.id -> MatchMode.ENDS_WITH
            else -> error("Unrecognised match mode")
        }
    }

    fun getSearchTarget(): SearchTarget {
        return when (radio_search_target.checkedRadioButtonId) {
            radio_search_target_dict.id -> SearchTarget.WORDS
            radio_search_target_examples.id -> SearchTarget.EXAMPLES
            else -> error("Unrecognised search target")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}