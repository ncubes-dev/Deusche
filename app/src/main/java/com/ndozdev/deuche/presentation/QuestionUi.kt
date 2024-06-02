package com.ndozdev.deuche.presentation

import android.annotation.SuppressLint
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.ndozdev.deuche.data.Question
import com.ndozdev.deuche.presentation.ui_utils.VoiceBubble
import com.ndozdev.deuche.R
import com.ndozdev.deuche.utils.Constants
import kotlinx.coroutines.selects.select

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun QuestionUi(
    question: Question,
    dummyQuestion: Question,
    speak: (String) -> Unit,
    viewModel: MainViewModel,
    index: Int,
    maxIndex: Int,
    mode: String,
    speed:Float
) {
    val answers = derivedStateOf { listOf(question.deWord) + dummyQuestion.deWord }.value.shuffled()
    var dropDown by remember { mutableStateOf(false) }
    var answerConfirmed by remember { mutableStateOf(false) }
    var isDialog by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(question.isSaved) }


    var selectedAnswer by remember {
        mutableStateOf("")
    }
    var correctAnswer by remember {
        mutableStateOf(question.deWord)
    }
    var isCorrect by remember {
        mutableStateOf<Boolean?>(null)
    }
    if (isDialog) {
        Dialog(onDismissRequest = { isDialog = false }) {
            Settings(onEvent = viewModel::onEvent, speed =speed )
        }
    }


    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { isSaved = !isSaved }) {
                    Icon(
                        painter = if (isSaved) painterResource(id = R.drawable.baseline_bookmark_24) else painterResource(
                            id = R.drawable.baseline_bookmark_border_24
                        ),
                        contentDescription = "book mark",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (index == maxIndex) {
                        } else {
                            viewModel.onEvent(MainScreenEvents.UpdateQuestion(question.copy(isSaved = isSaved)))
                            viewModel.onEvent(MainScreenEvents.NextQuestion(if (index == maxIndex) index else index + 1))
                        }
                        isSaved = question.isSaved
                    },
                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                        contentDescription = "next",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (index == 0) {
                        } else {
                            viewModel.onEvent(MainScreenEvents.UpdateQuestion(question.copy(isSaved = isSaved)))
                            viewModel.onEvent(MainScreenEvents.NextQuestion(if (index == 0) 0 else index - 1))
                        }
                        isSaved = question.isSaved
                    },
                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }


            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    val values = listOf<String>("All", "Bookmarks")
                    DropdownMenu(offset = DpOffset(x = Dp.Infinity, y = 0.dp), expanded = dropDown,
                        onDismissRequest = { dropDown = false }) {
                        values.forEach {
                            DropdownMenuItem(
                                modifier = Modifier.background(if (mode == it) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background),
                                text = { Text(text = it) },
                                onClick = {
                                    viewModel.onEvent(MainScreenEvents.FilterQuestion(it))
                                    dropDown = false
                                })
                        }
                    }
                    Text(
                        text = "(${index + 1} / ${maxIndex + 1}) What do you hear?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable { isDialog = true }
                            .padding(10.dp),

                        )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable { dropDown = true }
                            .padding(10.dp),

                        )

                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        VoiceBubble(text = question.deWord,
                            selectedAnswerState = selectedAnswer.isNotEmpty(),
                            speak = {
                                speak(question.deWord)
                            }
                        )
                    }
                }
                Text(
                    text = "Select the correct answer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                LazyRow {
                    items(answers) { answer ->
                        OutlinedButton(enabled = !answerConfirmed,
                            modifier = Modifier.padding(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedAnswer == answer) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = if (selectedAnswer == answer) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer,
                            ),
                            onClick = {
                                selectedAnswer = answer
                                isCorrect = answer == correctAnswer
                            }) {
                            Text(text = answer)
                        }
                    }
                }

                LazyColumn {
                    if (selectedAnswer.isNotEmpty()) {
                        item {
                            androidx.compose.material3.ListItem(
                                modifier = Modifier.clickable { speak(question.deSentence) },
                                tonalElevation = 3.dp,
                                shadowElevation = 3.dp,
                                headlineText = { Text(text = question.deWord) },
                                overlineText = { Text(text = question.enSentence) },
                                supportingText = {
                                    Text(text = question.deSentence)
                                })

                        }
                        item {
                            androidx.compose.material3.ListItem(
                                modifier = Modifier.clickable { speak(dummyQuestion.deSentence) },
                                tonalElevation = 3.dp,
                                shadowElevation = 3.dp,
                                headlineText = { Text(text = dummyQuestion.deWord) },
                                overlineText = { Text(text = dummyQuestion.enSentence) },
                                supportingText = {
                                    Text(text = dummyQuestion.deSentence)
                                })

                        }
                    }
                }
            }
        }
    }
}




