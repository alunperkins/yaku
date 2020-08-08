package com.alun.yaku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alun.common.models.DictEntry
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recentlyViewedListViewAdapter: DictEntryAdapter
    private lateinit var recentlyViewedListViewManager: LinearLayoutManager
    private lateinit var searchViewModel: SearchViewModel

    val words: Array<DictEntry> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // TODO get search string from intent
            val searchFragment =
                SearchFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_search_frame_layout, searchFragment)
                .commit()
        } else {
//            TODO("load saved instance state")
        }

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.search.observe(this, Observer {
            // TODO conditionally start SearchResultsActivity or replace a fragment depending on if search results fragment holder is in the xml
            it?.let {
                SearchResultsActivity.newInstance(this, it)
            }
        })

        initRecentlyViewedList()
    }

    private fun initRecentlyViewedList() {
        recentlyViewedListViewManager = LinearLayoutManager(this)

        recentlyViewedListViewAdapter = DictEntryAdapter(words)
        recentlyViewedListViewAdapter.clickListener = object : DictEntryAdapter.ClickListener {
            override fun onClick(position: Int) {
                println(
                    "MAIN ACTIVITY CLICK LISTENER " + position + "  " + words.get(position)
                        .toString()
                )
//                    search_src_text.setText(words.get(position).reading)
            }
        }

        list_recently_viewed.run {
            layoutManager = recentlyViewedListViewManager
            adapter = recentlyViewedListViewAdapter
            setHasFixedSize(true)
        }
    }
}