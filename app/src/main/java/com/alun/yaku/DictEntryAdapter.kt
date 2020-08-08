package com.alun.yaku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alun.common.models.DictEntry

class DictEntryAdapter(private val words: Array<DictEntry>) :
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
        holder.view.findViewById<TextView>(R.id.deli_japanese).text = words.get(position).toString()
        holder.view.findViewById<TextView>(R.id.deli_english).text = words.get(position).toString()
        println("CLICK LISTENER TO STRING " + clickListener.toString() + "   " + (clickListener == null))
        holder.view.setOnClickListener({ it: View? -> clickListener?.onClick(position) })
    }

    override fun getItemCount(): Int {
        return words.size
    }
}