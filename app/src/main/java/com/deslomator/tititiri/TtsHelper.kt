package com.deslomator.tititiri

import android.content.Context
import android.content.ContextWrapper
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class TtsHelper(context: Context) : ContextWrapper(context) {

    private  lateinit var textToSpeech: TextToSpeech
    var ttsHandler: TtsHandler
    var phrase: String? = null

    private fun speak(locution: String) {
            textToSpeech.speak(
                locution,
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
    }

    inner class TtsHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            val data = msg.data
            phrase = data.getString("locution")
        }
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
                phrase?.let { speak(locution = it) }
            } else {
                Log.e(TAG, "TTS Initialization Failed")
            }
        }

        val ttsThread = HandlerThread(
            "ttsThread",
            Process.THREAD_PRIORITY_URGENT_AUDIO
        )
        ttsThread.start()
        // Get the HandlerThread's Looper and use it for our Handler
        val ttsLooper = ttsThread.looper
        ttsHandler = TtsHandler(ttsLooper)
    }

    companion object {
        private const val TAG = "TtsHandler"
    }
}