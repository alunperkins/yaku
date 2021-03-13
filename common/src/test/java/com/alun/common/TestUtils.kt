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