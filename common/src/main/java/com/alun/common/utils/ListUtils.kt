package com.alun.common.utils

import java.lang.Error

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