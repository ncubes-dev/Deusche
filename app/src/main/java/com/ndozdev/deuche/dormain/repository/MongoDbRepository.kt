package com.ndozdev.deuche.dormain.repository



import com.ndozdev.deuche.data. QuestionRO
import kotlinx.coroutines.flow.Flow

interface MongoDBRepository {

    fun getQuestions(): Flow<List< QuestionRO>>

    suspend fun insertQuestion(question:  QuestionRO)

    suspend fun updateQuestion(question: QuestionRO)

}