package com.alun.yaku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_search_results.*

/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultsFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel

    // TODO: Rename and change types of parameters
    private var searchParams: SearchParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getParcelable(ARG_PARAM1)!!
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        activity?.let { it ->
            searchViewModel.search.observe(it, Observer { sp ->
                searchParams = sp
                temp_text_id.text = searchParams?.run {
                    text + ", " + searchMode + ", " + searchTarget + ", " + matchMode
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchResultsFragment()
    }
}