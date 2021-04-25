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
import com.alun.yaku.models.WordDetail
import com.alun.yaku.recyclerviewadapters.KanaAdapter
import com.alun.yaku.recyclerviewadapters.KanjiAdapter
import com.alun.yaku.recyclerviewadapters.SenseAdapter
import com.alun.yaku.viewmodels.EntrySelectedViewModel
import kotlinx.android.synthetic.main.fragment_word_detail.*

class WordDetailFragment : Fragment() {
    private val entrySelectedViewModel: EntrySelectedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        word_detail_list_kanji.run {
            layoutManager = LinearLayoutManager(context)
            adapter = KanjiAdapter(listOf())
        }

        word_detail_list_kana.run {
            layoutManager = LinearLayoutManager(context)
            adapter = KanaAdapter(listOf())
        }

        word_detail_list_senses.run {
            layoutManager = LinearLayoutManager(context)
            adapter = SenseAdapter(listOf())
        }

        entrySelectedViewModel.entrySelected.observe(viewLifecycleOwner, Observer { wordDetail: WordDetail? ->
            if (wordDetail == null) return@Observer
            val kanjis = wordDetail.dictEntry.kanjis
            if (kanjis != null) {
                word_detail_list_kanji.swapAdapter(KanjiAdapter(kanjis), true)
            }
            val kanas = wordDetail.dictEntry.kanas
            word_detail_list_kana.swapAdapter(KanaAdapter(kanas), true)
            val senses = wordDetail.dictEntry.senses
            word_detail_list_senses.swapAdapter(SenseAdapter(senses), true)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = WordDetailFragment() // TODO the constructor should take a WordDetail PARAMETER instead of the object OBSERVING the entrySelectedViewModel
    }
}