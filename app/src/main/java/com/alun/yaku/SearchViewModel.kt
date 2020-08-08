package com.alun.yaku

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val search = MutableLiveData<SearchParams>()
}