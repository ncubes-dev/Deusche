package com.ndozdev.deuche.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ndozdev.deuche.data.Question
import com.ndozdev.deuche.data.QuestionRO
import com.ndozdev.deuche.data.toQuestionRO

import com.ndozdev.deuche.dormain.repository.DataStoreRepository
import com.ndozdev.deuche.dormain.repository.MongoDBRepository
import com.ndozdev.deuche.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val mongodbRepository: MongoDBRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private var _questions = MutableStateFlow<List<QuestionRO>>(emptyList())
    val questions = _questions.asStateFlow()
    private var _index = MutableStateFlow<Int>(0)
    val index = _index.asStateFlow()
    private var _bookmarkIndex = MutableStateFlow<Int>(0)
    val bookmarkIndex = _bookmarkIndex.asStateFlow()
    private var _mode = MutableStateFlow<String>("All")
    val mode = _mode.asStateFlow()
    private var _speed = MutableStateFlow<Float>(1f)
    val speed = _speed.asStateFlow()
    init {
        viewModelScope.launch {
            launch {
                dataStoreRepository.getKeyValuePair("speed").collect{
                    if (it!=null){
                        _speed.value=it.toFloat()
                    }else{
                        _speed.value=1f
                    }
                }
            }
            launch {
                dataStoreRepository.getKeyValuePair("mode").collect {
                    if (it != null) {
                        _mode.value = it
                        mongodbRepository.getQuestions().collect { questionROS ->
                            _questions.value = if (it=="All") questionROS else questionROS.filter { it.isSaved }
                            if (questionROS.isEmpty()) {
                                putQuestions()
                            }
                        }
                    }else{
                        mongodbRepository.getQuestions().collect { questionROS ->
                            _questions.value = questionROS
                            if (questionROS.isEmpty()) {
                                putQuestions()
                            }
                        }
                        putBookMakeState("All")
                    }
                }
            }
            launch {
                dataStoreRepository.getKeyValuePair("index").collect {
                    if (it != null) {
                        _index.value = it.toInt()
                    }
                }
            }
            launch {
                dataStoreRepository.getKeyValuePair("bookmarkIndex").collect {
                    if (it != null) {
                        _bookmarkIndex.value = it.toInt()
                    }
                }
            }
        }
    }

    private suspend fun putQuestions() {
        for (i in Constants.question) {
            mongodbRepository.insertQuestion(i.toQuestionRO())
        }
    }

    private suspend fun putBookMakeState(state:String) {
        dataStoreRepository.putKeyValuePair("mode", state)
    }


    fun onEvent(event: MainScreenEvents) {
        when (event) {
            is MainScreenEvents.UpdateQuestion -> {
                viewModelScope.launch {
                    mongodbRepository.updateQuestion(event.question.toQuestionRO())
                }
            }
            is MainScreenEvents.UpdateSpeed -> {
                viewModelScope.launch {
                    dataStoreRepository.putKeyValuePair("speed",event.speed.toString())
                    _speed.value=event.speed
                }
            }

            is MainScreenEvents.NextQuestion -> {
                viewModelScope.launch {
                    if (_mode.value=="All"){
                        dataStoreRepository.putKeyValuePair("index",event.index.toString())
                    }else{
                        dataStoreRepository.putKeyValuePair("bookmarkIndex",event.index.toString())
                    }

                }
            }

            is MainScreenEvents.FilterQuestion -> {
                viewModelScope.launch {
                    mongodbRepository.getQuestions().collect { questionROS ->
                        if (event.selected=="All"){
                            _questions.value = questionROS
                            putBookMakeState("All")
                            _mode.value="All"
                        }else{
                            _questions.value = questionROS.filter { it.isSaved }
                            putBookMakeState("Bookmarks")
                            _mode.value="Bookmarks"
                        }
                    }
                }
            }

            else -> {
            }

        }

    }

}