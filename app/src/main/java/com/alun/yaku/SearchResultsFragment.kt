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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.alun.common.models.DictEntry
import kotlinx.android.synthetic.main.fragment_search_results.*

class SearchResultsFragment : Fragment() {
    private val searchResultsViewModel: SearchResultsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchParams: SearchParams? = arguments?.getParcelable<SearchParams?>(ARGS_KEY_SEARCH_PARAMS)

//        temp_text_id.text = searchParams?.run {
//            text + ", " + searchMode + ", " + searchTarget + ", " + matchMode
//        }

// useful for printing exceptions: `e.message + '\n' + StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString()`

        searchParams?.let {
            with(searchResultsViewModel) {
                search(context, searchParams)
                results.observe(
                    viewLifecycleOwner,
                    Observer<Result<List<DictEntry>>> { entries: Result<List<DictEntry>>? ->
                        temp_text_id.text = when (entries) {
                            is Result.Success -> entries.data.map { de -> de.kanas.map { k -> k.str }.joinToString(separator = "\n") }.joinToString(separator = "\n")
                            is Result.Error -> "there was an error" + entries.exception.localizedMessage
                            else -> "Loading"
                        }
                    })
            }
        }
    }

    companion object {
        private val ARGS_KEY_SEARCH_PARAMS = "args_key_search_params"

        @JvmStatic
        fun newInstance(searchParams: SearchParams): SearchResultsFragment {
            return SearchResultsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGS_KEY_SEARCH_PARAMS, searchParams)
                }
            }
        }
    }
}