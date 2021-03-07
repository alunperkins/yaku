package com.alun.common.utils

import java.util.*
import kotlin.collections.ArrayList

class RomajiParser {
    private enum class KanaTarget {
        HIRAGANA,
        KATAKANA,
        HIRAGANA_AND_KATAKANA,
        ;
    }

    companion object {

        private val hiraganaLookup: Map<String, List<String>> by lazy {
            mapOf(
                "a" to listOf("あ"),
                "ba" to listOf("ば"),
                "be" to listOf("べ"),
                "bi" to listOf("び"),
                "bo" to listOf("ぼ"),
                "bu" to listOf("ぶ"),
                "bya" to listOf("びゃ"),
                "byo" to listOf("びょ"),
                "byu" to listOf("びゅ"),
                "cha" to listOf("ちゃ"),
                "chi" to listOf("ち"),
                "cho" to listOf("ちょ"),
                "chu" to listOf("ちゅ"),
                "da" to listOf("だ"),
                "de" to listOf("で"),
                "di" to listOf("ぢ"), // non-standard romanisation, for input convenience only
                "do" to listOf("ど"),
                "du" to listOf("づ"), // non-standard romanisation, for input convenience only
                "e" to listOf("え"),
                "fu" to listOf("ふ"),
                "ga" to listOf("が"),
                "ge" to listOf("げ"),
                "gi" to listOf("ぎ"),
                "go" to listOf("ご"),
                "gu" to listOf("ぐ"),
                "gya" to listOf("ぎゃ"),
                "gyo" to listOf("ぎょ"),
                "gyu" to listOf("ぎゅ"),
                "ha" to listOf("は"),
                "he" to listOf("へ"),
                "hi" to listOf("ひ"),
                "ho" to listOf("ほ"),
                "hya" to listOf("ひゃ"),
                "hyo" to listOf("ひょ"),
                "hyu" to listOf("ひゅ"),
                "i" to listOf("い"),
                "ja" to listOf("じゃ"),
                "ja" to listOf("ぢゃ"),
                "ji" to listOf("じ", "ぢ"), // non-standard romanisation, for input convenience only
                "jo" to listOf("じょ", "ぢょ"), // non-standard romanisation, for input convenience only
                "ju" to listOf("じゅ", "ぢゅ"), // non-standard romanisation, for input convenience only
                "ka" to listOf("か"),
                "ke" to listOf("け"),
                "ki" to listOf("き"),
                "ko" to listOf("こ"),
                "ku" to listOf("く"),
                "kya" to listOf("きゃ"),
                "kyo" to listOf("きょ"),
                "kyu" to listOf("きゅ"),
                "ltu" to listOf("っ"), // non-standard romanisation, for input convenience only
                "lya" to listOf("ゃ"), // non-standard romanisation, for input convenience only
                "lyo" to listOf("ょ"), // non-standard romanisation, for input convenience only
                "lyu" to listOf("ゅ"), // non-standard romanisation, for input convenience only
                "ma" to listOf("ま"),
                "me" to listOf("め"),
                "mi" to listOf("み"),
                "mo" to listOf("も"),
                "mu" to listOf("む"),
                "mya" to listOf("みゃ"),
                "myo" to listOf("みょ"),
                "myu" to listOf("みゅ"),
                "n" to listOf("ん"), // non-standard romanisation, for input convenience only
                "na" to listOf("な"),
                "ne" to listOf("ね"),
                "ni" to listOf("に"),
                "nn" to listOf("ん"), // non-standard romanisation, for input convenience only
                "no" to listOf("の"),
                "nu" to listOf("ぬ"),
                "nya" to listOf("にゃ"),
                "nyo" to listOf("にょ"),
                "nyu" to listOf("にゅ"),
                "o" to listOf("お"),
                "pa" to listOf("ぱ"),
                "pe" to listOf("ぺ"),
                "pi" to listOf("ぴ"),
                "po" to listOf("ぽ"),
                "pu" to listOf("ぷ"),
                "pya" to listOf("ぴゃ"),
                "pyo" to listOf("ぴょ"),
                "pyu" to listOf("ぴゅ"),
                "ra" to listOf("ら"),
                "re" to listOf("れ"),
                "ri" to listOf("り"),
                "ro" to listOf("ろ"),
                "ru" to listOf("る"),
                "rya" to listOf("りゃ"),
                "ryo" to listOf("りょ"),
                "ryu" to listOf("りゅ"),
                "sa" to listOf("さ"),
                "se" to listOf("せ"),
                "sha" to listOf("しゃ"),
                "shi" to listOf("し"),
                "sho" to listOf("しょ"),
                "shu" to listOf("しゅ"),
                "si" to listOf("し"), // non-standard romanisation, for input convenience only
                "so" to listOf("そ"),
                "su" to listOf("す"),
                "ta" to listOf("た"),
                "te" to listOf("て"),
                "ti" to listOf("ち"), // non-standard romanisation, for input convenience only
                "to" to listOf("と"),
                "tsu" to listOf("つ"),
                "tu" to listOf("つ"), // non-standard romanisation, for input convenience only
                "u" to listOf("う"),
                "wa" to listOf("わ"),
                "xtu" to listOf("っ"), // non-standard romanisation, for input convenience only
                "ya" to listOf("や"),
                "yo" to listOf("よ"),
                "yu" to listOf("ゆ"),
                "za" to listOf("ざ"),
                "ze" to listOf("ぜ"),
                "zi" to listOf("じ"), // non-standard romanisation, for input convenience only
                "zo" to listOf("ぞ"),
                "zu" to listOf("ず", "づ"), // non-standard romanisation, for input convenience only
                "zya" to listOf("じゃ"), // non-standard romanisation, for input convenience only
                "zyo" to listOf("じょ"), // non-standard romanisation, for input convenience only
                "zyu" to listOf("じゅ") // non-standard romanisation, for input convenience only
            )
        }

        private val katakanaLookup: Map<String, List<String>> by lazy {
            mapOf(
                "a" to listOf("ア"),
                "ba" to listOf("バ"),
                "be" to listOf("ベ"),
                "bi" to listOf("ビ"),
                "bo" to listOf("ボ"),
                "bu" to listOf("ブ"),
                "bya" to listOf("ビャ"),
                "bye" to listOf("ビェ"),
                "byo" to listOf("ビョ"),
                "byu" to listOf("ビュ"),
                "cha" to listOf("チャ"),
                "che" to listOf("チェ"),
                "chi" to listOf("チ"),
                "cho" to listOf("チョ"),
                "chu" to listOf("チュ"),
                "da" to listOf("ダ"),
                "de" to listOf("デ"),
                "di" to listOf("ディ"),
                "do" to listOf("ド"),
                "du" to listOf("ドゥ"),
                "dyu" to listOf("デュ"),
                "e" to listOf("エ"),
                "fa" to listOf("ファ"),
                "fe" to listOf("フェ"),
                "fi" to listOf("フィ"),
                "fo" to listOf("フォ"),
                "fu" to listOf("フ"),
                "fya" to listOf("フャ"),
                "fye" to listOf("フィェ"),
                "fyo" to listOf("フョ"),
                "fyu" to listOf("フュ"),
                "ga" to listOf("ガ"),
                "ge" to listOf("ゲ"),
                "gi" to listOf("ギ"),
                "go" to listOf("ゴ"),
                "gu" to listOf("グ"),
                "gwa" to listOf("グァ", "グヮ"),
                "gwe" to listOf("グェ"),
                "gwi" to listOf("グィ"),
                "gwo" to listOf("グォ"),
                "gya" to listOf("ギャ"),
                "gye" to listOf("ギェ"),
                "gyo" to listOf("ギョ"),
                "gyu" to listOf("ギュ"),
                "ha" to listOf("ハ"),
                "he" to listOf("ヘ"),
                "hi" to listOf("ヒ"),
                "ho" to listOf("ホ"),
                "hu" to listOf("ホゥ"),
                "hya" to listOf("ヒャ"),
                "hye" to listOf("ヒェ"),
                "hyo" to listOf("ヒョ"),
                "hyu" to listOf("ヒュ"),
                "i" to listOf("イ"),
                "ja" to listOf("ジャ", "ヂャ"), // non-standard romanisation, for input convenience only
                "je" to listOf("ジェ"),
                "ji" to listOf("ジ", "ヂ"), // non-standard romanisation, for input convenience only
                "jo" to listOf("ジョ", "ヂョ"), // non-standard romanisation, for input convenience only
                "ju" to listOf("ジュ", "ヂュ"), // non-standard romanisation, for input convenience only
                "ka" to listOf("カ"),
                "ke" to listOf("ケ"),
                "ki" to listOf("キ"),
                "ko" to listOf("コ"),
                "ku" to listOf("ク"),
                "kwa" to listOf("クァ", "クヮ"),
                "kwe" to listOf("クェ"),
                "kwi" to listOf("クィ"),
                "kwo" to listOf("クォ"),
                "kya" to listOf("キャ"),
                "kye" to listOf("キェ"),
                "kyo" to listOf("キョ"),
                "kyu" to listOf("キュ"),
                "la" to listOf("ラ゜"),
                "le" to listOf("レ゜"),
                "li" to listOf("リ゜"),
                "lo" to listOf("ロ゜"),
                "lu" to listOf("ル゜"),
                "lya" to listOf("リ゜ャ"),
                "lye" to listOf("リ゜ェ"),
                "lyo" to listOf("リ゜ョ"),
                "lyu" to listOf("リ゜ュ"),
                "ma" to listOf("マ"),
                "me" to listOf("メ"),
                "mi" to listOf("ミ"),
                "mo" to listOf("モ"),
                "mu" to listOf("ム"),
                "mya" to listOf("ミャ"),
                "mye" to listOf("ミェ"),
                "myo" to listOf("ミョ"),
                "myu" to listOf("ミュ"),
                "n" to listOf("ン"), // non-standard romanisation, for input convenience only
                "na" to listOf("ナ"),
                "ne" to listOf("ネ"),
                "ni" to listOf("ニ"),
                "nn" to listOf("ン"), // non-standard romanisation, for input convenience only
                "no" to listOf("ノ"),
                "nu" to listOf("ヌ"),
                "nya" to listOf("ニャ"),
                "nye" to listOf("ニェ"),
                "nyo" to listOf("ニョ"),
                "nyu" to listOf("ニュ"),
                "o" to listOf("オ"),
                "pa" to listOf("パ"),
                "pe" to listOf("ペ"),
                "pi" to listOf("ピ"),
                "po" to listOf("ポ"),
                "pu" to listOf("プ"),
                "pya" to listOf("ピャ"),
                "pye" to listOf("ピェ"),
                "pyo" to listOf("ピョ"),
                "pyu" to listOf("ピュ"),
                "ra" to listOf("ラ"),
                "re" to listOf("レ"),
                "ri" to listOf("リ"),
                "ro" to listOf("ロ"),
                "ru" to listOf("ル"),
                "rya" to listOf("リャ"),
                "rye" to listOf("リェ"),
                "ryo" to listOf("リョ"),
                "ryu" to listOf("リュ"),
                "sa" to listOf("サ"),
                "se" to listOf("セ"),
                "sha" to listOf("シャ"),
                "she" to listOf("シェ"),
                "shi" to listOf("シ"),
                "sho" to listOf("ショ"),
                "shu" to listOf("シュ"),
                "si" to listOf("スィ"),
                "so" to listOf("ソ"),
                "su" to listOf("ス"),
                "ta" to listOf("タ"),
                "te" to listOf("テ"),
                "ti" to listOf("ティ"),
                "to" to listOf("ト"),
                "tsa" to listOf("ツァ"),
                "tse" to listOf("ツェ"),
                "tsi" to listOf("ツィ"),
                "tso" to listOf("ツォ"),
                "tsu" to listOf("ツ"),
                "tsyu" to listOf("ツュ"),
                "tu" to listOf("トゥ"),
                "tyu" to listOf("テュ"),
                "u" to listOf("ウ"),
                "va" to listOf("ヴァ"),
                "ve" to listOf("ヴェ"),
                "vi" to listOf("ヴィ"),
                "vo" to listOf("ヴォ"),
                "vu" to listOf("ヴ"),
                "vya" to listOf("ヴャ"),
                "vye" to listOf("ヴィェ"),
                "vyo" to listOf("ヴョ"),
                "vyu" to listOf("ヴュ"),
                "wa" to listOf("ウァ", "ワ"),
                "we" to listOf("ウェ"),
                "wi" to listOf("ウィ"),
                "wo" to listOf("ウォ"),
                "wu" to listOf("ウゥ"),
                "wyu" to listOf("ウュ"),
                "xtu" to listOf("ッ"), // non-standard romanisation, for input convenience only
                "ya" to listOf("ヤ"),
                "ye" to listOf("イェ"),
                "yi" to listOf("イィ"),
                "yo" to listOf("ヨ"),
                "yu" to listOf("ユ"),
                "za" to listOf("ザ"),
                "ze" to listOf("ゼ"),
                "zi" to listOf("ズィ"),
                "zo" to listOf("ゾ"),
                "zu" to listOf("ズ", "ヅ"), // non-standard romanisation, for input convenience only
                "zya" to listOf("ジャ"), // non-standard romanisation, for input convenience only
                "zyo" to listOf("ジョ"), // non-standard romanisation, for input convenience only
                "zyu" to listOf("ジュ") // non-standard romanisation, for input convenience only
            )
        }

        fun getHiraganaOptionsForRomaji(input: String): List<String> {
            return getKanaOptionsForRomaji(input, KanaTarget.HIRAGANA)
        }

        fun getKatakanaOptionsForRomaji(input: String): List<String> {
            return getKanaOptionsForRomaji(input, KanaTarget.KATAKANA)
        }

        fun getAllKanaOptionsForRomaji(input: String): List<String> {
            return getKanaOptionsForRomaji(input, KanaTarget.HIRAGANA_AND_KATAKANA)
        }

        private fun getKanaOptionsForRomaji(input: String, kanaTarget: KanaTarget): List<String> {
            val sections =
                StringUtils.splitAfterDelimiterKeepingDelimiter(
                    input.toLowerCase(Locale.ROOT),
                    "[^bcdfghjklmnpqrstvwxyz]"
                )
                    .map {
                        org.apache.commons.lang3.StringUtils.replaceEach(
                            it,
                            arrayOf(" ", "'", "-"),
                            arrayOf("", "", "")
                        )
                    }
                    .filter { it.isNotEmpty() }
            val hiraganaOptions = when (kanaTarget) {
                KanaTarget.KATAKANA -> listOf()
                KanaTarget.HIRAGANA, KanaTarget.HIRAGANA_AND_KATAKANA -> {
                    ListUtils.chooseFromEachList(sections.map(::sectionToHiragana))
                        .map { list: List<String> -> list.joinToString(separator = "") }
                }
            }
            val katakanaOptions = when (kanaTarget) {
                KanaTarget.HIRAGANA -> listOf()
                KanaTarget.KATAKANA, KanaTarget.HIRAGANA_AND_KATAKANA -> {
                    ListUtils.chooseFromEachList(sections.mapIndexed { i, s ->
                        sectionToKatakana(s, sections.elementAtOrNull(i - 1))
                    })
                        .map { list: List<String> -> list.joinToString(separator = "") }
                }
            }
            return ArrayList<String>().apply {
                addAll(hiraganaOptions)
                addAll(katakanaOptions)
            }
        }

        /**
         * parameter romajiSection is a string of 0..n consonants with 0..1 vowels at the end, all lowercase
         * so it should match regex `[bcdfghjklmnpqrstvwxyz]*[aeiou]?`
         * it may not be valid romaji
         * if it is valid romaji, returns list of possible hiragana strings it could represent, each of which may be multiple characters/mora
         * else return the string unchanged
         */
        private fun sectionToHiragana(section: String): List<String> {
            if (!AlphabetDetector.isLatin(section)) return listOf(section)
            assertStringHasExpectedFormForKanaConversion(section) // fail fast, useful for testing, should never happen because this is a private method
            val romaji = section
            return when {
                hasSokuonPrefix(romaji) -> {
                    val moraCandidate = romaji.substring(1)
                    getHiraganaOrOriginal(moraCandidate).map { "っ$it" }
                }
                isNNBeforeAnNLineMora(romaji) -> {
                    val moraCandidate = romaji.replace(Regex("^[nm]*"), "n")
                    getHiraganaOrOriginal(moraCandidate).map { "ん$it" }
                }
                isNNBeforeANonNLineMora(romaji) -> {
                    val moraCandidate = romaji.replace(Regex("^[nm]*"), "")
                    getHiraganaOrOriginal(moraCandidate).map { "ん$it" }
                }
                else -> {
                    val moraCandidate = romaji
                    getHiraganaOrOriginal(moraCandidate)
                }
            }
        }

        private fun sectionToKatakana(section: String, previousSection: String?): List<String> {
            if (!AlphabetDetector.isLatin(section)) return listOf(section)
            assertStringHasExpectedFormForKanaConversion(section) // fail fast, useful for testing, should never happen because this is a private method
            val romajiSection = section
            val isSingleVowelCharAndPreviousSectionEndedWithSameSingleVowelChar =
                romajiSection.matches(Regex("[aeiou]"))
                        && previousSection?.endsWith(romajiSection) ?: false
            return when {
                isSingleVowelCharAndPreviousSectionEndedWithSameSingleVowelChar -> {
                    arrayListOf("ー").apply { addAll(getKatakanaOrOriginal(romajiSection)) }
                }
                hasSokuonPrefix(romajiSection) -> {
                    val moraCandidate = romajiSection.substring(1)
                    getKatakanaOrOriginal(moraCandidate).map { "ッ$it" }
                }
                isNNBeforeAnNLineMora(romajiSection) -> {
                    val moraCandidate = romajiSection.replace(Regex("^[nm]*"), "n")
                    getKatakanaOrOriginal(moraCandidate).map { "ン$it" }
                }
                isNNBeforeANonNLineMora(romajiSection) -> {
                    val moraCandidate = romajiSection.replace(Regex("^[nm]*"), "")
                    getKatakanaOrOriginal(moraCandidate).map { "ン$it" }
                }
                else -> {
                    val moraCandidate = romajiSection
                    getKatakanaOrOriginal(moraCandidate)
                }
            }
        }

        private fun assertStringHasExpectedFormForKanaConversion(s: String) {
            if (!s.matches(Regex("[bcdfghjklmnpqrstvwxyz]*[aeiou]?"))) error("not valid romaji section input: $s")
        }

        /**
         * "sokuon" is a っ or ッ, often rendered in romaji input using a doubled consonant (particular consonants only), requiring special logic to handle.
         */
        private fun hasSokuonPrefix(romajiSection: String): Boolean {
            return romajiSection.indexOfAny(listOf("kk", "gg", "ss", "zz", "tt", "tc", "dd", "bb", "pp")) == 0
        }

        /**
         * is ん or ン before a non-n-line mora
         */
        private fun isNNBeforeANonNLineMora(romajiSection: String): Boolean {
            return romajiSection.matches(Regex("^[nm]+[^nmaeiouy].*"))
        }

        /**
         * is ん or ン before an n-line mora
         */
        private fun isNNBeforeAnNLineMora(romajiSection: String): Boolean {
            return romajiSection.matches(Regex("^[nm]+[nm][aeiouy].*"))
        }

        private fun getHiraganaOrOriginal(s: String): List<String> = hiraganaLookup.getOrElse(s, { listOf(s) })
        private fun getKatakanaOrOriginal(s: String): List<String> = katakanaLookup.getOrElse(s, { listOf(s) })
    }
}
