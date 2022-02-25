package com.deslomator.tititiri.model

import kotlin.random.Random

data class Frecuencia(
    val memoria: Int,
    private val frecuenciaEntero: Int,
    private val frecuenciaDecimal: Int,
    private val zonas: List<String>,
) {
    private fun frecuenciaDecimaltoString(): String {
        return when (frecuenciaDecimal) {
            in 0..9 -> "00$frecuenciaDecimal"
            in 10..99 -> "0$frecuenciaDecimal"
            else -> frecuenciaDecimal.toString()
        }
    }
    var frecuencia = "$frecuenciaEntero.${frecuenciaDecimaltoString()}"
    private var zonaElegida: Int = 0
    fun elegirZona() {
        zonaElegida = Random.nextInt(zonas.size)
//        Log.d("elegirZona()", "zona elegida: $zonaElegida, zonaDropdown: ${zonaDropdown()} zonaTts: ${zonaTts()}")
    }
    fun zonaDropdown(): String {
        return zonas[zonaElegida]
            .replace("T 4", "T4")
            .replace("4 S", "4S")
            .replace("T 2", "T2")
    }

    val numeroTts: String = "memoria $memoria"
    fun frecuenciaTts(): String {
        val cero = when (frecuenciaDecimal) {
            in 1..9 -> "cero cero"
            in 10..99 -> "cero"
            else -> ""
        }
        return "pase a $frecuenciaEntero coma $cero $frecuenciaDecimal"
    }
    fun zonaTts(): String { return "está en ${zonaDropdown()}" }

}

