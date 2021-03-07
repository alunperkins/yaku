package com.alun.common.utils

import org.junit.Assert
import org.junit.Test

internal class StringUtilsTest {
    @Test
    fun shouldInsertBreaksAfterDelimiterAndKeepDelimeterItself() {
        val input = "THERE IS A PROBLEM STOP COME AT ONCE STOP AWAIT YOUR REPLY"
        val delimiterRegex = "STOP "
        val expected = listOf("THERE IS A PROBLEM STOP ", "COME AT ONCE STOP ", "AWAIT YOUR REPLY")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldBeCompatibleWithMultipleDelimiters() {
        val input = "ABCDFEGHIJKLMNOPQRSTUVWXYZ"
        val delimiterRegex = "[BDW]"
        val expected = listOf("AB", "CD", "FEGHIJKLMNOPQRSTUVW", "XYZ")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldBeCompatibleWithMultipleMultiCharactersDelimiters() {
        val input = "aXXaXXaXanewlineaXXaXXaXa"
        val delimiterRegex = "XX|newline"
        val expected = listOf("aXX", "aXX", "aXanewline", "aXX", "aXX", "aXa")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldSplitOnConsecutiveDelimiters() {
        val input = "niisan"
        val delimiterRegex = "[aeiou]"
        val expected = listOf("ni", "i", "sa", "n")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldCopeWithDelimeterAtVeryStart() {
        val input = "oniisan"
        val delimiterRegex = "[aeiou]"
        val expected = listOf("o", "ni", "i", "sa", "n")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldCopeWithDelimeterAtVeryEnd() {
        val input = "desu"
        val delimiterRegex = "[aeiou]"
        val expected = listOf("de", "su")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldCopeWithNoDelimitersAtAll() {
        val input = "xxx"
        val delimiterRegex = "[aeiou]"
        val expected = listOf("xxx")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldMapEmptyStringToEmptyList() {
        val input = ""
        val delimiterRegex = "[aeiou]"
        val expected = listOf<String>()
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }

    /**
     * not sure this is needed TBH, after all, why would it treat spaces specially?
     */
    @Test
    fun shouldNotTreatSpacesSpecially() {
        val input = " oniisan ha   ureshii desu "
        val delimiterRegex = "[aeiou]"
        val expected = listOf(" o", "ni", "i", "sa", "n ha", "   u", "re", "shi", "i", " de", "su", " ")
        val actual = StringUtils.splitAfterDelimiterKeepingDelimiter(input, delimiterRegex)
        Assert.assertEquals(expected, actual)
    }
}