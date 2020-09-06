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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.alun.yaku.R
import com.alun.yaku.models.MatchMode
import com.alun.yaku.models.SearchMode
import com.alun.yaku.models.SearchParams
import com.alun.yaku.models.SearchTarget
import com.alun.yaku.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchText(searchViewModel.text.value!!)
        setRadioMatchMode(searchViewModel.matchMode.value!!)
        setRadioSearchTarget(searchViewModel.searchTarget.value!!)

        search_src_text.addTextChangedListener { searchViewModel.text.postValue(getSearchText()) }
        radio_match_mode.setOnCheckedChangeListener { _, _ -> searchViewModel.matchMode.postValue(getRadioMatchMode()) }
        radio_search_target.setOnCheckedChangeListener { _, _ ->
            searchViewModel.searchTarget.postValue(getRadioSearchTarget())
        }

        search_btn_from_english.setOnClickListener { vw -> onClickSearchFromEnglish(vw) }
        search_btn_from_japanese.setOnClickListener { vw -> onClickSearchFromJapanese(vw) }
        search_btn_from_japanese_definflecting.setOnClickListener { vw -> onClickSearchFromJapaneseDeinflecting(vw) }
        search_src_text_clear.setOnClickListener { vw -> onClickClearSearchString() }
    }

    private fun getSearchText(): String {
        return search_src_text.text.toString().trim()
    }

    private fun setSearchText(s: String) {
        search_src_text.setText(s)
    }

    private fun getRadioMatchMode(): MatchMode {
        return when (radio_match_mode.checkedRadioButtonId) {
            radio_match_mode_any.id -> MatchMode.ANY
            radio_match_mode_exact.id -> MatchMode.EXACT
            radio_match_mode_starts_with.id -> MatchMode.STARTS_WITH
            radio_match_mode_ends_with.id -> MatchMode.ENDS_WITH
            else -> error("Unrecognised match mode")
        }
    }

    private fun setRadioMatchMode(matchMode: MatchMode) {
        radio_match_mode_any.isChecked = matchMode == MatchMode.ANY
        radio_match_mode_starts_with.isChecked = matchMode == MatchMode.STARTS_WITH
        radio_match_mode_ends_with.isChecked = matchMode == MatchMode.ENDS_WITH
        radio_match_mode_exact.isChecked = matchMode == MatchMode.EXACT
    }

    private fun getRadioSearchTarget(): SearchTarget {
        return when (radio_search_target.checkedRadioButtonId) {
            radio_search_target_dict.id -> SearchTarget.WORDS
            radio_search_target_examples.id -> SearchTarget.EXAMPLES
            else -> error("Unrecognised search target")
        }
    }

    private fun setRadioSearchTarget(searchTarget: SearchTarget) {
        radio_search_target_dict.isChecked = searchTarget == SearchTarget.WORDS
        radio_search_target_examples.isChecked = searchTarget == SearchTarget.EXAMPLES
    }

    private fun onClickClearSearchString() {
        search_src_text.setText("")
    }

    private fun onClickSearchFromEnglish(view: View) {
        search(SearchMode.FROM_ENGLISH)
    }

    private fun onClickSearchFromJapanese(view: View) {
        search(SearchMode.FROM_JAPANESE)
    }

    private fun onClickSearchFromJapaneseDeinflecting(view: View) {
        search(SearchMode.FROM_JAPANESE_DEINFLECTED)
    }

    private fun search(searchMode: SearchMode) {
        if (searchViewModel.text.value.isNullOrBlank()) return
        searchViewModel.executedSearch.postValue(
            SearchParams(
                searchViewModel.text.value!!,
                searchMode,
                searchViewModel.matchMode.value!!,
                searchViewModel.searchTarget.value!!
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}