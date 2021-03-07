package com.alun.common.utils

import org.junit.Assert
import org.junit.Test

internal class ListUtilsTest {
    @Test
    fun shouldReturnEmptyListForEmptyList() {
        val expected = listOf<List<String>>()
        val actual = ListUtils.chooseFromEachList(listOf<List<String>>())
        Assert.assertEquals(expected, actual)
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
        Assert.assertEquals(expected, actual)
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
        Assert.assertEquals(allPossibleSentencesExpected, allPossibleSentencesActual)
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
        Assert.assertEquals(allPossibleSentencesExpected, allPossibleSentencesActual)
    }
}