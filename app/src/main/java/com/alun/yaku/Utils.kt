package com.alun.yaku

import android.view.View

class Utils {
    companion object {
        fun goneIfNull(x: Any?): Int {
            return when (x) {
                null -> View.GONE
                else -> View.VISIBLE
            }
        }
    }
}