package com.alun.common

import org.junit.Assert

class TestUtils {
    companion object {
        /**
         * don't seem to be able to get hold of "assertFailsWith" from Junit 5 for some compatibility reason
         */
        fun assertThrows(fn: () -> Unit): Exception {
            try {
                fn()
                Assert.fail("Expected an exception to be thrown, but nothing was thrown")
                error("unreachable")
            } catch (e: Exception) {
                return e
            }
        }

        fun assertListEquals(expected: List<String>, actual: List<String>) {
            Assert.assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
        }
    }
}