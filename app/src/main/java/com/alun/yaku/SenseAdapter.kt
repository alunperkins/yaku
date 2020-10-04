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
import com.alun.common.models.Reference
import com.alun.common.models.Sense

class SenseAdapter(private val senses: List<Sense>) :
    RecyclerView.Adapter<SenseAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sense_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sense = senses[position]
        holder.view.run {
            sense.pos?.let {
                findViewById<TextView>(R.id.sense_list_item_poss).run {
                    text = it.joinToString(separator = ", ") { it.abbr }
                    visibility = View.VISIBLE
                }
            }

            sense.miscs?.let {
                findViewById<TextView>(R.id.sense_list_item_miscs).run {
                    text = it.joinToString(separator = ", ") { it.abbr }
                    visibility = View.VISIBLE
                }
            }

            sense.fields?.let {
                findViewById<TextView>(R.id.sense_list_item_fields).run {
                    text = it.joinToString(separator = ", ") { it.abbr }
                    visibility = View.VISIBLE
                }
            }

            val restrictions: List<String>? = listOf(sense.stagks ?: listOf(), sense.stagrs ?: listOf())
                .flatten()
                .ifEmpty { null }
            if (restrictions != null)
                findViewById<TextView>(R.id.sense_list_item_restrs).run {
                    text =
                        context.getString(
                            R.string.parenthetic_restriction,
                            restrictions.joinToString(separator = ", ")
                        )
                    visibility = View.VISIBLE
                }

            sense.infos?.let {
                findViewById<TextView>(R.id.sense_list_item_infos).run {
                    text = it.joinToString(separator = ", ")
                    visibility = View.VISIBLE
                }
            }

            sense.dialects?.let {
                findViewById<TextView>(R.id.sense_list_item_dialects).run {
                    text = it.joinToString(separator = ", ") { it.desc }
                    visibility = View.VISIBLE
                }
            }

            sense.loanSources?.let {
                findViewById<TextView>(R.id.sense_list_item_loan_sources).run {
                    text = context.getString(R.string.parenthetic_loan_source,
                        it.joinToString(separator = ", ") { it.lang.threeLetterCode })
                    visibility = View.VISIBLE
                }
            }

            findViewById<TextView>(R.id.sense_list_item_glosses).text =
                sense.glosses.joinToString(separator = ", ") { "${it.str}${if (it.type == null) "" else " (" + it.type!!.abbr + ")"}" } // TODO visual different between the `str` text and the `type` text

            sense.xrefs?.let {
                findViewById<TextView>(R.id.sense_list_item_xrefs).run {
                    text = context.getString(R.string.parenthetic_see_also, referencesString(it))
                    visibility = View.VISIBLE
                }
            }

            sense.antonyms?.let {
                findViewById<TextView>(R.id.sense_list_item_antonyms).run {
                    text = context.getString(R.string.parenthetic_see_antonyms, referencesString(it))
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return senses.size
    }

    private fun referencesString(refs: List<Reference>): String {
        return refs.joinToString(separator = ", ") {
            when (it.kanji.isNullOrBlank()) {
                true -> when (it.kana.isNullOrBlank()) {
                    true -> "ERR" // TODO handle differently
                    false -> it.kana!!
                }
                false -> when (it.kana.isNullOrBlank()) {
                    true -> it.kanji!!
                    false -> it.kanji + "/" + it.kana
                }
            }
        }
    }
}