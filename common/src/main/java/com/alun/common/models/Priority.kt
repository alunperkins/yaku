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

enum class Priority(val s: String) {
    GAI1("gai1"),
    GAI2("gai2"),
    ICHI1("ichi1"),
    ICHI2("ichi2"),
    NEWS1("news1"),
    NEWS2("news2"),
    NF01("nf01"),
    NF02("nf02"),
    NF03("nf03"),
    NF04("nf04"),
    NF05("nf05"),
    NF06("nf06"),
    NF07("nf07"),
    NF08("nf08"),
    NF09("nf09"),
    NF10("nf10"),
    NF11("nf11"),
    NF12("nf12"),
    NF13("nf13"),
    NF14("nf14"),
    NF15("nf15"),
    NF16("nf16"),
    NF17("nf17"),
    NF18("nf18"),
    NF19("nf19"),
    NF20("nf20"),
    NF21("nf21"),
    NF22("nf22"),
    NF23("nf23"),
    NF24("nf24"),
    NF25("nf25"),
    NF26("nf26"),
    NF27("nf27"),
    NF28("nf28"),
    NF29("nf29"),
    NF30("nf30"),
    NF31("nf31"),
    NF32("nf32"),
    NF33("nf33"),
    NF34("nf34"),
    NF35("nf35"),
    NF36("nf36"),
    NF37("nf37"),
    NF38("nf38"),
    NF39("nf39"),
    NF40("nf40"),
    NF41("nf41"),
    NF42("nf42"),
    NF43("nf43"),
    NF44("nf44"),
    NF45("nf45"),
    NF46("nf46"),
    NF47("nf47"),
    NF48("nf48"),
    SPEC1("spec1"),
    SPEC2("spec2"),
    ;

    companion object {
        private val mapping = values().associateBy(Priority::s)
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Priority")
    }
}
