package com.alun.common.utils

class StringUtils {
    companion object {
        fun splitAfterDelimiterKeepingDelimiter(s: String, delimiterRegex: String): List<String> {
            val split = s.split(Regex("(?<=" + delimiterRegex + ")")).toMutableList()
            return if (split.size > 0 && split.last() == "")
                split.subList(0, split.size - 1)
            else
                split
        }
    }
}