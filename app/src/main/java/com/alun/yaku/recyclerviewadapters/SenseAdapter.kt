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
import com.alun.common.models.Reference
import com.alun.common.models.Sense
import com.alun.yaku.R
import com.alun.yaku.Utils.Companion.goneIfNull
import kotlinx.android.synthetic.main.sense_list_item.view.*

class SenseAdapter(private val senses: List<Sense>) :
    RecyclerView.Adapter<SenseAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var expandedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sense_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sense = senses[position]
        val expanded = expandedPosition == position

        holder.view.run {
            setOnClickListener {
                when (expandedPosition) {
                    null -> {
                        expandedPosition = position
                        notifyItemChanged(position)
                    }
                    position -> {
                        expandedPosition = null
                        notifyItemChanged(position)
                    }
                    else -> {
                        val expandedPositionPrev = expandedPosition!!
                        expandedPosition = position
                        notifyItemChanged(expandedPositionPrev)
                        notifyItemChanged(position)
                    }
                }
            }

            sense_list_item_layout_collapsed.visibility = if (expanded) View.GONE else View.VISIBLE
            sense_list_item_layout_expanded.visibility = if (expanded) View.VISIBLE else View.GONE
            sense_list_item_arrowhead.text = if (expanded) "\u02C5" else "\u02C3"

            if (expanded) {
                findViewById<TextView>(R.id.sense_list_item_verbose_poss).run {
                    text = sense.pos?.joinToString(separator = "\n") { "${it.abbr}: ${it.desc}" }
                    visibility = goneIfNull(sense.pos)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_miscs).run {
                    text = sense.miscs?.joinToString(separator = "\n") { "${it.abbr}: ${it.desc}" }
                    visibility = goneIfNull(sense.miscs)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_fields).run {
                    text = sense.fields?.joinToString(separator = "\n") { "${it.abbr}: ${it.desc}" }
                    visibility = goneIfNull(sense.fields)
                }

                val restrictions: List<String>? = listOf(sense.stagks ?: listOf(), sense.stagrs ?: listOf())
                    .flatten()
                    .ifEmpty { null }
                findViewById<TextView>(R.id.sense_list_item_verbose_restrs).run {
                    text = restrictions?.let {
                        context.getString(
                            R.string.parenthetic_restriction,
                            it.joinToString(separator = ", ")
                        )
                    }
                    visibility = goneIfNull(restrictions)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_infos).run {
                    text = sense.infos?.joinToString(separator = "\n")
                    visibility = goneIfNull(sense.infos)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_dialects).run {
                    text = sense.dialects?.joinToString(separator = "\n") { "${it.abbr}: ${it.desc}" }
                    visibility = goneIfNull(sense.dialects)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_loan_sources).run {
                    text = sense.loanSources?.let {
                        context.getString(
                            R.string.parenthetic_loan_source,
                            it.joinToString(separator = ", ") { it.lang.threeLetterCode })
                    }
                    visibility = goneIfNull(sense.loanSources)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_glosses).text =
                    sense.glosses.joinToString(separator = "\n") { "${it.str}${if (it.type == null) "" else " (" + it.type!!.abbr + ")"}" } // TODO visual different between the `str` text and the `type` text

                findViewById<TextView>(R.id.sense_list_item_verbose_xrefs).run {
                    text = sense.xrefs?.let { context.getString(R.string.parenthetic_see_also, referencesString(it)) }
                    visibility = goneIfNull(sense.xrefs)
                }

                findViewById<TextView>(R.id.sense_list_item_verbose_antonyms).run {
                    text = sense.antonyms?.let {
                        context.getString(R.string.parenthetic_see_antonyms, referencesString(it))
                    }
                    visibility = goneIfNull(sense.antonyms)
                }
            } else {
                findViewById<TextView>(R.id.sense_list_item_poss).run {
                    text = sense.pos?.joinToString(separator = ", ") { it.abbr }
                    visibility = goneIfNull(sense.pos)
                }

                findViewById<TextView>(R.id.sense_list_item_miscs).run {
                    text = sense.miscs?.joinToString(separator = ", ") { it.abbr }
                    visibility = goneIfNull(sense.miscs)
                }

                findViewById<TextView>(R.id.sense_list_item_fields).run {
                    text = sense.fields?.joinToString(separator = ", ") { it.abbr }
                    visibility = goneIfNull(sense.fields)
                }

                val restrictions: List<String>? = listOf(sense.stagks ?: listOf(), sense.stagrs ?: listOf())
                    .flatten()
                    .ifEmpty { null }
                findViewById<TextView>(R.id.sense_list_item_restrs).run {
                    text = restrictions?.let {
                        context.getString(
                            R.string.parenthetic_restriction,
                            it.joinToString(separator = ", ")
                        )
                    }
                    visibility = goneIfNull(restrictions)
                }

                findViewById<TextView>(R.id.sense_list_item_infos).run {
                    text = sense.infos?.joinToString(separator = ", ")
                    visibility = goneIfNull(sense.infos)
                }

                findViewById<TextView>(R.id.sense_list_item_dialects).run {
                    text = sense.dialects?.joinToString(separator = ", ") { it.abbr }
                    visibility = goneIfNull(sense.dialects)
                }

                findViewById<TextView>(R.id.sense_list_item_loan_sources).run {
                    text = sense.loanSources?.let {
                        context.getString(
                            R.string.parenthetic_loan_source,
                            it.joinToString(separator = ", ") { it.lang.threeLetterCode })
                    }
                    visibility = goneIfNull(sense.loanSources)
                }

                findViewById<TextView>(R.id.sense_list_item_glosses).text =
                    sense.glosses.joinToString(separator = ", ") { "${it.str}${if (it.type == null) "" else " (" + it.type!!.abbr + ")"}" } // TODO visual different between the `str` text and the `type` text

                findViewById<TextView>(R.id.sense_list_item_xrefs).run {
                    text = sense.xrefs?.let { context.getString(R.string.parenthetic_see_also, referencesString(it)) }
                    visibility = goneIfNull(sense.xrefs)
                }

                findViewById<TextView>(R.id.sense_list_item_antonyms).run {
                    text = sense.antonyms?.let {
                        context.getString(R.string.parenthetic_see_antonyms, referencesString(it))
                    }
                    visibility = goneIfNull(sense.antonyms)
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