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
package com.alun.common.utils

class ListUtils {
    companion object {
        /**
         * takes a list `myList` where each element `myList[i]` is a list of options for `resultList[i]`, and returns a list of all possible resultLists
         * e.g. input
         *   [
         *     ["Good", "Bad"],
         *     ["idea", "cop", "dog"]
         *   ]
         * goes to output
         *   [
         *     ["Good", "idea"],
         *     ["Good", "cop"],
         *     ["Good", "dog"],
         *     ["Bad", "idea"],
         *     ["Bad", "cop"],
         *     ["Bad", "dog"]
         *   ]
         *
         * Implementation uses recursion.
         * Have not assessed the efficiency of the implementation. TODO assess efficiency and see if it's worth spending time optimizing it
         */
        fun <T> chooseFromEachList(listOfOptionsForEachListPosition: List<List<T>>): List<List<T>> {
            if (listOfOptionsForEachListPosition.isEmpty()) return listOf()
            val firstElement: List<T> = listOfOptionsForEachListPosition[0]
            if (firstElement.isEmpty()) error("Invalid input - there are no possibilities for one of the positions")
            val subsequentElements: List<List<T>> = listOfOptionsForEachListPosition.subList(1, listOfOptionsForEachListPosition.size)
            if (subsequentElements.isEmpty()) return firstElement.map { listOf(it) }
            val allOptionsForTheSubsequentElements: List<List<T>> = chooseFromEachList(subsequentElements) // recursion!

            return firstElement.flatMap { optionForFirstElement ->
                allOptionsForTheSubsequentElements.map { anOptionForTheSubsequentElements ->
                    prependElement(optionForFirstElement, anOptionForTheSubsequentElements)
                }
            }
        }

        private fun <T> prependElement(element: T, list: List<T>): List<T> {
            return mutableListOf(element).also {
                it.addAll(list)
            }
        }
    }
}