package com.deslomator.tititiri.data

import com.deslomator.tititiri.model.Frequency

object DataSource {
    val frecuencias = listOf(
        Frequency( 1, 121, 705, listOf("plataforma T 2 sur")),
        Frequency( 2, 121, 855, listOf("plataforma T 2 norte")),
        Frequency( 3, 121, 755, listOf("plataforma T 4 S norte")),
        Frequency( 5, 118, 155, listOf("pista 14 derecha", "pista 32 izquierda")),
        Frequency( 6, 118, 80, listOf("pista 18 derecha", "pista 36 izquierda")),
        Frequency( 8, 121, 980, listOf("rodadura central sur")),
        Frequency( 10, 121, 680, listOf("plataforma T 4 S sur")),
        Frequency( 11, 122, 980, listOf("bomberos")),
        Frequency( 12, 118, 680, listOf("pista 18 izquierda", "pista 36 derecha")),
        Frequency( 13, 118, 980, listOf("pista 14 izquierda", "pista 32 derecha")),
        Frequency( 14, 123, 155, listOf("rodadura central norte")),
        Frequency( 16, 123, 255, listOf("plataforma T 4 norte")),
        Frequency( 17, 123, 5, listOf("plataforma T 4 sur")),
        Frequency( 19, 121, 500, listOf("emergencia")),
    )
}