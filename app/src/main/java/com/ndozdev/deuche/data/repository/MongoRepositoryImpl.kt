package com.ndozdev.deuche.data.repository


import com.ndozdev.deuche.data.QuestionRO
import com.ndozdev.deuche.dormain.repository.MongoDBRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MongoRepositoryImpl(val realm: Realm) : MongoDBRepository {

    override fun getQuestions(): Flow<List<QuestionRO>> =realm.query< QuestionRO>().asFlow().map { it.list }
    override suspend fun insertQuestion(question: QuestionRO) {
        realm.write { copyToRealm(question) }
    }
    override suspend fun updateQuestion(questionRO: QuestionRO) {
        realm.write {
            val queriedLocalUser = query< QuestionRO>(query = "_id == $0", questionRO._id).first().find()
            queriedLocalUser?.numberOfTries = questionRO.numberOfTries
            queriedLocalUser?.score = questionRO.score
            queriedLocalUser?.isSaved = questionRO.isSaved
     
        }
    }


}