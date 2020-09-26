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
package com.alun.indexcreator;

import com.alun.common.models.DictEntry;
import com.alun.jmdictparser.DictionaryProvider;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        final String pathJmDictXml = Objects.requireNonNull(args[0]);
        System.out.println("pathJmDictXml = " + pathJmDictXml);
        final List<DictEntry> entries = new DictionaryProvider().run(pathJmDictXml);

        final String pathIndexDir = Objects.requireNonNull(args[1]);
        System.out.println("pathIndexDir = " + pathIndexDir);
        new IndexCreator().run(entries, pathIndexDir);
    }
}