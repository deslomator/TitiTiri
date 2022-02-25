package com.deslomator.tititiri

import android.content.Context
//import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class Frecuencia(
    val id: Int,
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

class FrecuenciasModel : ViewModel() {

    private var selectedIndex = 0
    private fun selectIndex() {
        selectedIndex = Random.nextInt(Src.frecuencias.size - 1) + 1
    }

    var selectedTipo by mutableStateOf(Type.MEMORY)
        private set
    var memoriaSeleccionada by mutableStateOf(0)
        private set
    var frecuenciaSeleccionada by mutableStateOf(0)
        private set
    var zonaSeleccionada by mutableStateOf(0)
        private set
    var showGood by mutableStateOf(false)
        private set
    var showBad by mutableStateOf(false)
        private set

    fun onMemoriaChanged(newValue: Int) {
        memoriaSeleccionada = newValue
    }

    fun onFrecuenciaChanged(newValue: Int) {
        frecuenciaSeleccionada = newValue
    }

    fun onZonaChanged(newValue: Int) {
        zonaSeleccionada = newValue
    }

    private fun selectZonas() {
        for (item in Src.frecuencias) item.elegirZona()
    }

    private fun selectedItem(): Frecuencia {
        return Src.frecuencias[selectedIndex]
    }

    private fun selectTipo() {
        selectedTipo = Type.getRandom()
    }

    fun scrambledFreqs(): List<Frecuencia> {
        val list: MutableList<Frecuencia> = mutableListOf()
        list.add(Src.frecuencias[0])
        while (list.size < Src.frecuencias.size) {
            val index = Random.nextInt(Src.frecuencias.size - 1) + 1
            val item = Src.frecuencias[index]
            if (!list.contains(item)) list.add(item)
        }
        return list
    }

    fun setNewPregunta(context: Context) {
        selectZonas()
        selectIndex()
        selectTipo()

        memoriaSeleccionada = -1
        frecuenciaSeleccionada = -1
        zonaSeleccionada = -1
        when (selectedTipo) {
            Type.MEMORY -> memoriaSeleccionada = selectedIndex
            Type.FREQUENCY -> frecuenciaSeleccionada = selectedIndex
            Type.ZONE -> zonaSeleccionada = selectedIndex
        }
        showBad = false
        showGood = false

        val locution = when (selectedTipo) {
            Type.MEMORY -> selectedItem().numeroTts
            Type.FREQUENCY -> selectedItem().frecuenciaTts()
            Type.ZONE -> selectedItem().zonaTts()
        }
        sendTtsMessage(context = context, locution = locution)
    }

    private fun sendTtsMessage(context: Context, locution: String) {
//        Log.d("sendTtsMessage()", "inicializando, locution: $locution")
        if (locution.length > 1) {
            TtsHelper(context = context, locution = locution)
        }
    }

    fun checkAnswer() {
        val goodIndex = when (selectedTipo) {
            Type.MEMORY -> memoriaSeleccionada
            Type.FREQUENCY -> frecuenciaSeleccionada
            Type.ZONE -> zonaSeleccionada
        }
        if (memoriaSeleccionada == goodIndex
            && frecuenciaSeleccionada == goodIndex
            && zonaSeleccionada == goodIndex
        ) {
            showBad = false
            showGood = true
        } else {
            showBad = true
            showGood = false
        }
    }
}

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

