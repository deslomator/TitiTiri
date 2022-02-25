package com.deslomator.tititiri

import android.content.Context
import android.content.ContextWrapper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*

class TtsHelper(context: Context, private val locution: String = "") :
    ContextWrapper(context) {

    private  lateinit var textToSpeech: TextToSpeech

    private fun speak(locution: String) {
        textToSpeech.speak(
            locution,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    init {
        textToSpeech = TextToSpeech(context) { status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.w(TAG, "Current default language not supported")
                }
                if (locution != "") speak(locution)
            } else {
                Log.e(TAG, "TTS Initialization Failed")
            }
        }
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onError(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                textToSpeech.shutdown()
            }
        })
    }

    companion object {
        private const val TAG = "TtsHandler"
    }
}