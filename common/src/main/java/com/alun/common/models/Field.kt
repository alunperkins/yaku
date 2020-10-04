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
package com.alun.common.models

enum class Field(val abbr: String, val desc: String) {
    ANAT("anat", "anatomical term"),
    ARCHIT("archit", "architecture term"),
    ASTRON("astron", "astronomy, etc. term"),
    BASEB("baseb", "baseball term"),
    BIOL("biol", "biology term"),
    BOT("bot", "botany term"),
    BUDDH("Buddh", "Buddhist term"),
    BUS("bus", "business term"),
    CHEM("chem", "chemistry term"),
    CHRISTN("Christn", "Christian term"),
    COMP("comp", "computer terminology"),
    ECON("econ", "economics term"),
    ENGR("engr", "engineering term"),
    FINC("finc", "finance term"),
    FOOD("food", "food term"),
    GEOL("geol", "geology, etc. term"),
    GEOM("geom", "geometry term"),
    LAW("law", "law, etc. term"),
    LING("ling", "linguistics terminology"),
    MA("MA", "martial arts term"),
    MAHJ("mahj", "mahjong term"),
    MATH("math", "mathematics"),
    MED("med", "medicine, etc. term"),
    MIL("mil", "military"),
    MUSIC("music", "music term"),
    PHYSICS("physics", "physics terminology"),
    SHINTO("Shinto", "Shinto term"),
    SHOGI("shogi", "shogi term"),
    SPORTS("sports", "sports term"),
    SUMO("sumo", "sumo term"),
    ZOOL("zool", "zoology term"),
    ;

    companion object {
        private val mapping = values().associateBy(Field::desc)
        fun fromStr(desc: String) = mapping[desc] ?: error("Look up failed for \"$desc\" in Field")
    }
}
