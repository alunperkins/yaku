package com.alun.common.models

enum class Field(val s: String, val desc: String) {
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
        fun fromStr(s: String) = mapping[s] ?: error("Look up failed for \"$s\" in Field")
    }
}
