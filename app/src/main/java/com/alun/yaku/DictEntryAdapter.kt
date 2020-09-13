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
import com.alun.common.models.DictEntry

class DictEntryAdapter(private val entries: List<DictEntry>) :
    RecyclerView.Adapter<DictEntryAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ClickListener {
        fun onClick(position: Int)
    }

    var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dict_entry_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]

        val kanaString: String = entry.kanas.joinToString(separator = ", ") { k -> k.str }
        val kanjiString: String? = entry.kanjis?.joinToString(separator = ", ") { k -> k.str }
        holder.view.findViewById<TextView>(R.id.deli_japanese).text = when(kanjiString) {
            null -> holder.view.context.getString(R.string.dict_entry_list_item_kana, kanaString)
            else -> holder.view.context.getString(R.string.dict_entry_list_item_kanji_then_kana, kanjiString, kanaString)
        }
        holder.view.findViewById<TextView>(R.id.deli_english).text =
            entry.senses.joinToString(separator = ", ") { sense ->
                sense.glosses.joinToString(separator = "/") { g -> g.str }
            }

        holder.view.setOnClickListener { clickListener?.onClick(position) }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}