package com.deslomator.tititiri.data

import com.deslomator.tititiri.model.Frecuencia

object DataSource {
    val frecuencias = mapOf(
        0 to Frecuencia( 1, 121, 705, listOf("plataforma T 2 sur")),
        1 to Frecuencia( 2, 121, 855, listOf("plataforma T 2 norte")),
        2 to Frecuencia( 3, 121, 755, listOf("plataforma T 4 S norte")),
        3 to Frecuencia( 5, 118, 155, listOf("pista 14 derecha", "pista 32 izquierda")),
        4 to Frecuencia( 6, 118, 80, listOf("pista 18 derecha", "pista 36 izquierda")),
        5 to Frecuencia( 8, 121, 980, listOf("rodadura central sur")),
        6 to Frecuencia( 10, 121, 680, listOf("plataforma T 4 S sur")),
        7 to Frecuencia( 11, 122, 980, listOf("bomberos")),
        8 to Frecuencia( 12, 118, 680, listOf("pista 18 izquierda", "pista 36 derecha")),
        9 to Frecuencia( 13, 118, 980, listOf("pista 14 izquierda", "pista 32 derecha")),
        10 to Frecuencia( 14, 123, 155, listOf("rodadura central norte")),
        11 to Frecuencia( 16, 123, 255, listOf("plataforma T 4 norte")),
        12 to Frecuencia( 17, 123, 5, listOf("plataforma T 4 sur")),
        13 to Frecuencia( 19, 121, 500, listOf("emergencia")),
    )
}