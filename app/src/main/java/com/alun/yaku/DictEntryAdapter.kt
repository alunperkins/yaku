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
import com.alun.common.models.Lang

class DictEntryAdapter(private val words: List<DictEntry>) :
    RecyclerView.Adapter<DictEntryAdapter.ViewHolder>() {

    private val kanaSizeLimit = 2;
    private val glossesPerSenseLimit = 2;
    private val glossesOverallLimit = 4;

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
        val kanas = words[position].kanas
        holder.view.findViewById<TextView>(R.id.deli_japanese).text = kanas
            .take(kanaSizeLimit)
            .joinToString(
                separator = ", ",
                transform = { kana -> kana.str },
                postfix = if(kanas.size > kanaSizeLimit) "... (" + (kanas.size - kanaSizeLimit) + " more)" else ""
            )

        var availableGlossesCount = 0
        var tooManyGlosses = false
        val glosses = words[position]
            .senses
            .filter { sense -> sense.glosses != null }
            .flatMap { sense ->
                val glossesForThisSense = sense.glosses!!
                    .filter { gloss -> gloss.lang == Lang.ENG } // TODO use user-selected language
                    .filter { gloss -> gloss.str.isNotEmpty() }
                availableGlossesCount += glossesForThisSense.size
                if (glossesForThisSense.size > glossesPerSenseLimit) tooManyGlosses = true
                glossesForThisSense.take(glossesPerSenseLimit)
            }
        if (glosses.size > glossesOverallLimit) tooManyGlosses = true
        val displayedGlosses = glosses.take(glossesOverallLimit)
        holder.view.findViewById<TextView>(R.id.deli_english).text = displayedGlosses
            .joinToString(
                separator = ", ",
                transform = { gloss -> gloss.str },
                postfix = if(tooManyGlosses) "...(" + (availableGlossesCount - displayedGlosses.size) + " more)" else ""
            )
        println("==== CLICK LISTENER TO STRING " + clickListener.toString() + "   " + (clickListener == null))
        holder.view.setOnClickListener { clickListener?.onClick(position) }
    }

    override fun getItemCount(): Int {
        return words.size
    }
}