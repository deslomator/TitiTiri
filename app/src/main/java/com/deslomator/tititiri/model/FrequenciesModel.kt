package com.deslomator.tititiri.model

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.deslomator.tititiri.TtsHelper
import com.deslomator.tititiri.data.DataSource
import java.util.*
import kotlin.random.Random

class FrequenciesModel : ViewModel() {

    private var selectedQuestionId: UUID = DataSource.frecuencias[0].id
    private fun selectQuestionId() {
        val rnd = Random.nextInt(DataSource.frecuencias.size)
        selectedQuestionId = DataSource.frecuencias[rnd].id
        Log.d("", "selectQuestionId(), selectedQuestionId: $selectedQuestionId")
    }

    var selectedType by mutableStateOf(Type.MEMORY)
        private set

    var showTextMemory by mutableStateOf(Show.DEFAULT)
        private set
    var showTextFrequency by mutableStateOf(Show.DEFAULT)
        private set
    var showTextZone by mutableStateOf(Show.DEFAULT)
        private set

    var idMemorySelectedDropdown: UUID by mutableStateOf(selectedQuestionId)
        private set
    var idFrequencySelectedDropdown: UUID by mutableStateOf(selectedQuestionId)
        private set
    var idZoneSelectedDropdown: UUID by mutableStateOf(selectedQuestionId)
        private set
    var isCorrect by mutableStateOf(true)
        private set
    var showResult by mutableStateOf(false)
        private set

    fun onDropdownMemoryItemClicked(newValue: UUID) {
        idMemorySelectedDropdown = newValue
        showResult = false
        showTextMemory = Show.ITEM
    }

    fun onDropdownFrequencyItemClicked(newValue: UUID) {
        idFrequencySelectedDropdown = newValue
        showResult = false
        showTextFrequency = Show.ITEM
    }

    fun onDropdownZoneItemClicked(newValue: UUID) {
        idZoneSelectedDropdown = newValue
        showResult = false
        showTextZone = Show.ITEM
    }

    private fun selectZones() {
        for (item in DataSource.frecuencias) item.pickZone()
    }

    private fun getFrequencyById(id: UUID): Frequency {
        return DataSource.frecuencias.first { it.id == id }
    }

    private fun questionItem(): Frequency {
        return getFrequencyById(selectedQuestionId)
    }

    fun dropdownSelectedMemory(): String {
        return getFrequencyById(idMemorySelectedDropdown).memory.toString()
    }
    fun dropdownSelectedFrequency(): String {
        return getFrequencyById(idFrequencySelectedDropdown).frequency
    }
    fun dropdownSelectedZone(): String {
        return getFrequencyById(idZoneSelectedDropdown).zoneDropdown()
    }

    private fun selectType() {
        selectedType = Type.getRandom()
    }

    private fun scrambledList(
        map: (Frequency) -> Pair<UUID, String>
    ): List<Pair<UUID, String>> {
        val list = mutableListOf<Pair<UUID, String>>()
        val src = DataSource.frecuencias.map(map)
        while (list.size < src.size) {
            val item = src.random()
            if (!list.contains(item)) list.add(item)
        }
        return list
    }

    fun scrambledMems(): List<Pair<UUID, String>> {
        return scrambledList { Pair(it.id, it.memory.toString()) }
    }

    fun scrambledFreqs(): List<Pair<UUID, String>> {
        return scrambledList { Pair(it.id, it.frequency) }
    }

    fun scrambledZones(): List<Pair<UUID, String>> {
        return scrambledList { Pair(it.id, it.zoneDropdown()) }
    }

    fun setNewQuestion(context: Context) {
        selectZones()
        selectQuestionId()
        selectType()

        when (selectedType) {
            Type.MEMORY -> {
                showTextMemory = Show.ITEM
                showTextFrequency = Show.EMPTY
                showTextZone = Show.EMPTY
                idMemorySelectedDropdown = selectedQuestionId
            }
            Type.FREQUENCY -> {
                showTextMemory = Show.EMPTY
                showTextFrequency = Show.ITEM
                showTextZone = Show.EMPTY
                idFrequencySelectedDropdown = selectedQuestionId
            }
            Type.ZONE -> {
                showTextMemory = Show.EMPTY
                showTextFrequency = Show.EMPTY
                showTextZone = Show.ITEM
                idZoneSelectedDropdown = selectedQuestionId
            }
        }

        showResult = false
        isCorrect = false

        val locution = when (selectedType) {
            Type.MEMORY -> questionItem().memoryTts
            Type.FREQUENCY -> questionItem().frequencyTts()
            Type.ZONE -> questionItem().zoneTts()
        }
//        sendTtsMessage(context = context, locution = locution)
    }

    private fun sendTtsMessage(context: Context, locution: String) {
//        Log.d("sendTtsMessage()", "inicializando, locution: $locution")
        if (locution.length > 1) {
            TtsHelper(context = context, locution = locution)
        }
    }

    fun checkAnswer() {
        val goodIndex = when (selectedType) {
            Type.MEMORY -> idMemorySelectedDropdown
            Type.FREQUENCY -> idFrequencySelectedDropdown
            Type.ZONE -> idZoneSelectedDropdown
        }
        isCorrect = (idMemorySelectedDropdown == goodIndex
                && idFrequencySelectedDropdown == goodIndex
                && idZoneSelectedDropdown == goodIndex)
        showResult = true
    }
}