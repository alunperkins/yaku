package com.alun.yaku

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchParams(
    val text: String,
    val searchMode: SearchMode,
    val matchMode: MatchMode,
    val searchTarget: SearchTarget
) : Parcelable