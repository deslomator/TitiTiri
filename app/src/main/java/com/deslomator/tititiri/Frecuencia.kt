package com.deslomator.tititiri

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class Frecuencia(
    val id: Int,
    val numero: Int,
    val frecuenciaEntero: Int,
    val frecuenciaDecimal: Int,
    val zona1: String,
    val zona2: String = ""
) {
    var frecuencia = "$frecuenciaEntero.$frecuenciaDecimal"
    var frecuenciaPregunta = "pase a $frecuenciaEntero.$frecuenciaDecimal"

    var zonaElegida: Int = 0
        private set
    fun elegirZona() { zonaElegida = if (zona2 == "") 0 else Random.nextInt(2) }
    fun zona(): String { return if (zonaElegida == 0) zona1 else zona2 }

    val numeroTts: String = "memoria $numero"
    fun frecuenciaTts(): String {
        val cero = when (frecuenciaDecimal) {
            in 1..9 -> "cero cero"
            in 10..99 -> "cero"
            else -> ""
        }
        return "pase a $frecuenciaEntero coma $cero $frecuenciaDecimal"
    }
    var zonaTts: String = "está en ${if (zonaElegida == 0) zona1 else zona2}"

}

val frecuencias = listOf<Frecuencia>(
    Frecuencia(0,0, 0, 0, ""),
    Frecuencia(1,1, 121, 705, "plataforma T 2 sur"),
    Frecuencia(2,2, 121, 855, "plataforma T 2 norte"),
    Frecuencia(3,3, 121, 755, "plataforma T 4 S norte"),
    Frecuencia(4,5, 118, 155, "pista 14 derecha", "pista 32 izquierda"),
    Frecuencia(5,6, 118, 80, "pista 18 derecha", "pista 36 izquierda"),
    Frecuencia(6,8, 121, 980, "rodadura central sur"),
    Frecuencia(7,10, 121, 680, "plataforma T 4 S sur"),
    Frecuencia(8,11, 122, 980, "frecuencia bomberos"),
    Frecuencia(9,12, 118, 680, "pista 18 izquierda", "pista 36 derecha"),
    Frecuencia(10,13, 118, 980, "pista 14 izquierda", "pista 32 derecha"),
    Frecuencia(11,14, 123, 155, "rodadura central norte"),
    Frecuencia(12,16, 123, 255, "plataforma T 4 norte"),
    Frecuencia(13,17, 123, 5, "plataforma T 4 sur"),
    Frecuencia(14,19, 121, 500, "frecuencia emergencia"),
)

class SeleccionViewModel : ViewModel() {

    var pregunta by mutableStateOf(-1)
    var tipo by mutableStateOf(-1)
    var textoPregunta by mutableStateOf("pregunta")

    var memoriaSeleccionada by mutableStateOf(0)
    var frecuenciaSeleccionada by mutableStateOf(0)
    var zonaSeleccionada by mutableStateOf(0)
    var showGood by mutableStateOf(false)
    var showBad by mutableStateOf(false)
}
