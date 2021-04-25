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

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ListUtilsTest {
    @Test
    fun shouldReturnEmptyListForEmptyList() {
        val expected = listOf<List<String>>()
        val actual = ListUtils.chooseFromEachList(listOf<List<String>>())
        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnListOfFirstElementOptionsForSingleElementInput() {
        val optionsForFirstElement = listOf("Hello.", "Hi.", "Morning.")
        val expected = listOf(
            listOf("Hello."),
            listOf("Hi."),
            listOf("Morning.")
        )
        val actual = ListUtils.chooseFromEachList(listOf(optionsForFirstElement))
        assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnOnlyChoiceWhenThereIsOnlyOneChoice() {
        val optionsForFirstElement = listOf("A ")
        val optionsForSecondElement = listOf("cat ")
        val optionsForThirdElement = listOf("was ")
        val optionsForFourthElement = listOf("eating.")
        val allPossibleSentencesExpected = listOf(
            listOf("A ", "cat ", "was ", "eating.")
        )
        val allPossibleSentencesActual = ListUtils.chooseFromEachList(listOf(optionsForFirstElement, optionsForSecondElement, optionsForThirdElement, optionsForFourthElement))
        assertEquals(allPossibleSentencesExpected, allPossibleSentencesActual)
    }

    @Test
    fun shouldListAllWaysOfChoosingElementsAtEachPosition() {
        val optionsForFirstElement = listOf("A ", "The ")
        val optionsForSecondElement = listOf("cat ", "dog ", "hamster ")
        val optionsForThirdElement = listOf("was ")
        val optionsForFourthElement = listOf("eating.", "drinking.")
        val allPossibleSentencesExpected = listOf(
            listOf("A ", "cat ", "was ", "eating."),
            listOf("A ", "cat ", "was ", "drinking."),
            listOf("A ", "dog ", "was ", "eating."),
            listOf("A ", "dog ", "was ", "drinking."),
            listOf("A ", "hamster ", "was ", "eating."),
            listOf("A ", "hamster ", "was ", "drinking."),
            listOf("The ", "cat ", "was ", "eating."),
            listOf("The ", "cat ", "was ", "drinking."),
            listOf("The ", "dog ", "was ", "eating."),
            listOf("The ", "dog ", "was ", "drinking."),
            listOf("The ", "hamster ", "was ", "eating."),
            listOf("The ", "hamster ", "was ", "drinking.")
        )
        val allPossibleSentencesActual = ListUtils.chooseFromEachList(listOf(optionsForFirstElement, optionsForSecondElement, optionsForThirdElement, optionsForFourthElement))
        assertEquals(allPossibleSentencesExpected, allPossibleSentencesActual)
    }
}