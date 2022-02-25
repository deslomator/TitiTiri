package com.deslomator.tititiri.data

import com.deslomator.tititiri.model.Frecuencia

object Src {
    val frecuencias = listOf(
        Frecuencia(0, 0, 0, 0, listOf("")),
        Frecuencia(1, 1, 121, 705, listOf("plataforma T 2 sur")),
        Frecuencia(2, 2, 121, 855, listOf("plataforma T 2 norte")),
        Frecuencia(3, 3, 121, 755, listOf("plataforma T 4 S norte")),
        Frecuencia(4, 5, 118, 155, listOf("pista 14 derecha", "pista 32 izquierda")),
        Frecuencia(5, 6, 118, 80, listOf("pista 18 derecha", "pista 36 izquierda")),
        Frecuencia(6, 8, 121, 980, listOf("rodadura central sur")),
        Frecuencia(7, 10, 121, 680, listOf("plataforma T 4 S sur")),
        Frecuencia(8, 11, 122, 980, listOf("bomberos")),
        Frecuencia(9, 12, 118, 680, listOf("pista 18 izquierda", "pista 36 derecha")),
        Frecuencia(10, 13, 118, 980, listOf("pista 14 izquierda", "pista 32 derecha")),
        Frecuencia(11, 14, 123, 155, listOf("rodadura central norte")),
        Frecuencia(12, 16, 123, 255, listOf("plataforma T 4 norte")),
        Frecuencia(13, 17, 123, 5, listOf("plataforma T 4 sur")),
        Frecuencia(14, 19, 121, 500, listOf("emergencia")),
    )
}