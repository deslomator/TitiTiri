package com.deslomator.tititiri.model

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.deslomator.tititiri.TtsHelper
import com.deslomator.tititiri.data.DataSource
import kotlin.random.Random

class FrecuenciasModel : ViewModel() {

    private var selectedId = 0
    private fun selectId() {
        selectedId = Random.nextInt(DataSource.frecuencias.size)
    }

    var selectedTipo by mutableStateOf(Type.MEMORY)
        private set

    // -2 shows empty mySurface, -1 shows default string
    var idMemoriaSeleccionada by mutableStateOf(-1)
        private set
    var idFrecuenciaSeleccionada by mutableStateOf(-1)
        private set
    var idZonaSeleccionada by mutableStateOf(-1)
        private set
    var isCorrect by mutableStateOf(true)
        private set
    var showResult by mutableStateOf(false)
        private set

    fun onMemoriaChanged(newValue: Int) {
        idMemoriaSeleccionada = newValue
        showResult = false
    }

    fun onFrecuenciaChanged(newValue: Int) {
        idFrecuenciaSeleccionada = newValue
        showResult = false
    }

    fun onZonaChanged(newValue: Int) {
        idZonaSeleccionada = newValue
        showResult = false
    }

    private fun selectZonas() {
        for (item in DataSource.frecuencias) item.value.elegirZona()
    }

    private fun selectedItem(): Frecuencia? {
        return DataSource.frecuencias[selectedId]
    }

    private fun selectTipo() {
        selectedTipo = Type.getRandom()
    }

    fun scrambledMems(): Map<Int, String> {
        val list = mutableMapOf<Int, String>()
        val src = DataSource.frecuencias.map { it.key to it.value.memoria.toString() }.toMap()
        while (list.size < src.size) {
            Log.d("", "list.size: ${list.size}")
            val key = Random.nextInt(src.size)
            if (!list.containsKey(key)) src[key]?.let { list.put(key, it) }
        }
        return list
    }

    fun scrambledFreqs(): Map<Int, String> {
        val list = mutableMapOf<Int, String>()
        val src = DataSource.frecuencias.map { it.key to it.value.frecuencia }.toMap()
        while (list.size < src.size) {
            Log.d("", "list.size: ${list.size}")
            val key = Random.nextInt(src.size)
            if (!list.containsKey(key)) src[key]?.let { list.put(key, it) }
        }
        return list
    }

    fun scrambledZones(): Map<Int, String> {
        val list = mutableMapOf<Int, String>()
        val src = DataSource.frecuencias.map { it.key to it.value.zonaDropdown() }.toMap()
        while (list.size < src.size) {
            Log.d("", "list.size: ${list.size}")
            val key = Random.nextInt(src.size)
            if (!list.containsKey(key)) src[key]?.let { list.put(key, it) }
        }
        return list
    }

    fun setNewPregunta(context: Context) {
        selectZonas()
        selectId()
        selectTipo()

        idMemoriaSeleccionada = -2
        idFrecuenciaSeleccionada = -2
        idZonaSeleccionada = -2
        when (selectedTipo) {
            Type.MEMORY -> idMemoriaSeleccionada = selectedId
            Type.FREQUENCY -> idFrecuenciaSeleccionada = selectedId
            Type.ZONE -> idZonaSeleccionada = selectedId
        }

        showResult = false
        isCorrect = false

        val locution = when (selectedTipo) {
            Type.MEMORY -> selectedItem()?.numeroTts
            Type.FREQUENCY -> selectedItem()?.frecuenciaTts()
            Type.ZONE -> selectedItem()?.zonaTts()
        }
        sendTtsMessage(context = context, locution = locution?: "")
    }

    private fun sendTtsMessage(context: Context, locution: String) {
//        Log.d("sendTtsMessage()", "inicializando, locution: $locution")
        if (locution.length > 1) {
            TtsHelper(context = context, locution = locution)
        }
    }

    fun checkAnswer() {
        val goodIndex = when (selectedTipo) {
            Type.MEMORY -> idMemoriaSeleccionada
            Type.FREQUENCY -> idFrecuenciaSeleccionada
            Type.ZONE -> idZonaSeleccionada
        }
        isCorrect = (idMemoriaSeleccionada == goodIndex
                && idFrecuenciaSeleccionada == goodIndex
                && idZonaSeleccionada == goodIndex)
        showResult = true
    }
}