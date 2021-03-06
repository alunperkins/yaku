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
package com.alun.yaku.recyclerviewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alun.common.models.Kana
import com.alun.yaku.R
import com.alun.yaku.Utils.Companion.goneIfNull

class KanaAdapter(private val kanas: List<Kana>) :
    RecyclerView.Adapter<KanaAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kana_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kana = kanas[position]
        holder.view.run {
            findViewById<TextView>(R.id.kana_list_item_kana).text = kana.str

            findViewById<TextView>(R.id.kana_list_item_only_kanjis).run {
                text = kana.onlyForKanjis?.let {
                    context.getString(
                        R.string.parenthetic_restriction,
                        it.joinToString(", ")
                    )
                }
                visibility = goneIfNull(kana.onlyForKanjis)
            }

            when (kana.noKanji) {
                true -> findViewById<TextView>(R.id.kana_list_item_nokanji).run {
                    text = context.getString(R.string.parenthetic_warning) // or could use "⚠" ?
                    visibility = View.VISIBLE
                }
                else -> findViewById<TextView>(R.id.kana_list_item_nokanji).run {
                    text = null
                    visibility = View.GONE
                }
            }

            findViewById<TextView>(R.id.kana_list_item_infos).run {
                text = kana.infos?.let { it.joinToString(separator = ",") { it.abbr } }
                visibility = goneIfNull(kana.infos)
            }

            findViewById<TextView>(R.id.kana_list_item_pris).run {
                text = kana.priorities?.let { it.joinToString(separator = ",") { it.s } }
                visibility = goneIfNull(kana.priorities)
            }
        }
    }

    override fun getItemCount(): Int {
        return kanas.size
    }
}