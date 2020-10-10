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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alun.common.models.Kanji
import com.alun.yaku.Utils.Companion.goneIfNull

class KanjiAdapter(private val kanjis: List<Kanji>) :
    RecyclerView.Adapter<KanjiAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kanji_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kanji = kanjis[position]
        
        holder.view.run {
            findViewById<TextView>(R.id.kanji_list_item_kanji).text = kanji.str

            findViewById<TextView>(R.id.kanji_list_item_infos).run {
                text = kanji.infos?.let { it.joinToString(separator = ", ") { it.abbr } }
                visibility = goneIfNull(kanji.infos)
            }

            findViewById<TextView>(R.id.kanji_list_item_pris).run {
                text = kanji.priorities?.let { it.joinToString(separator = ", ") { it.s } }
                visibility = goneIfNull(kanji.priorities)
            }
        }
    }

    override fun getItemCount(): Int {
        return kanjis.size
    }
}