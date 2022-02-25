package com.deslomator.tititiri.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.deslomator.tititiri.TtsHelper
import com.deslomator.tititiri.data.Src
import kotlin.random.Random

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