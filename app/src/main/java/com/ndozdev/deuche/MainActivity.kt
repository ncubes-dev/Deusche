package com.ndozdev.deuche

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndozdev.deuche.data.toQuestion
import com.ndozdev.deuche.ui.theme.DeucheTheme
import com.ndozdev.deuche.presentation.MainViewModel
import com.ndozdev.deuche.presentation.QuestionUi
import com.ndozdev.deuche.presentation.viewModelFactory
import java.util.Locale
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeTTS()
        setContent {

            val viewModel = viewModel<MainViewModel>(
                factory = viewModelFactory {
                    MainViewModel(
                        MyApp.appModule.mongodbRepository,
                        MyApp.appModule.dataStoreRepository,
                    )
                }
            )
            val mode by viewModel.mode.collectAsState()
            val questions by viewModel.questions.collectAsState()
            val index by viewModel.index.collectAsState()
            val speed by viewModel.speed.collectAsState()
            val bookmarkIndex by viewModel.bookmarkIndex.collectAsState()
            DeucheTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        if (questions.isNotEmpty()) {
                            val question = questions[index].toQuestion()
                            val randomValue = Random.nextInt(0, questions.size-1)
                            val realIndex=if (mode=="All") index else bookmarkIndex
                            QuestionUi(
                                question = question,
                                dummyQuestion = questions[randomValue].toQuestion(),
                                speak = { word -> speak(word,speed) },
                                viewModel = viewModel,
                                index=realIndex,
                                maxIndex=questions.size-1,
                                mode = mode,
                                speed=speed
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initializeTTS() {
        tts = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.GERMAN
            }
        }
    }

    private fun speak(text: String,speed:Float) {
        if (tts?.isLanguageAvailable(Locale.GERMAN) == TextToSpeech.LANG_AVAILABLE) {
            tts?.language = Locale.GERMAN
            tts?.setSpeechRate(speed)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            Toast.makeText(applicationContext, "Language not available", Toast.LENGTH_SHORT).show()
        }
    }
}
































