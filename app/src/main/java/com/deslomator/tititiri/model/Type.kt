package com.deslomator.tititiri.model

import kotlin.random.Random

enum class Type(val value: Int) {
    MEMORY(0),
    FREQUENCY(1),
    ZONE(2);

    companion object {
        private val map = values().associateBy(Type::value)
        operator fun get(value: Int) = map[value]
        fun getRandom(): Type { return get(Random.nextInt(values().size))!! }
    }
}

enum class Show {EMPTY, DEFAULT, ITEM}