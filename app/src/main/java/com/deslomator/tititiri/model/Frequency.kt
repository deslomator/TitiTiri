package com.deslomator.tititiri.model

import java.util.*
import kotlin.random.Random

data class Frequency(
    val memory: Int,
    private val frequencyInteger: Int,
    private val frequencyDecimal: Int,
    private val zones: List<String>,
) {
    val id: UUID = UUID.randomUUID()

    private fun frequencyDecimalString(): String {
        return when (frequencyDecimal) {
            in 0..9 -> "00$frequencyDecimal"
            in 10..99 -> "0$frequencyDecimal"
            else -> frequencyDecimal.toString()
        }
    }
    var frequency = "$frequencyInteger.${frequencyDecimalString()}"
    private var pickedZone: Int = 0
    fun pickZone() {
        pickedZone = Random.nextInt(zones.size)
    }
    fun zoneDropdown(): String {
        return zones[pickedZone]
            .replace("T 4", "T4")
            .replace("4 S", "4S")
            .replace("T 2", "T2")
    }

    val memoryTts: String = "memoria $memory"
    fun frequencyTts(): String {
        val zero = when (frequencyDecimal) {
            in 1..9 -> "cero cero"
            in 10..99 -> "cero"
            else -> ""
        }
        return "pase a $frequencyInteger coma $zero $frequencyDecimal"
    }
    fun zoneTts(): String { return "está en ${zoneDropdown()}" }

}

