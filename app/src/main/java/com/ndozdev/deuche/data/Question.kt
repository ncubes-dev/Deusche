package com.ndozdev.deuche.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

data class Question(
    val id:String="",
    val deWord: String,
    val enMeaning: String,
    val enSentence: String,
    val deSentence: String,
    var numberOfTries: Int = 0,
    var score: Int = 0,
    var isSaved: Boolean = false
)

class QuestionRO : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var deWord: String=""
    var enMeaning: String=""
    var enSentence: String=""
    var deSentence: String=""
    var numberOfTries: Int = 0
    var score: Int = 0
    var isSaved: Boolean = false

}
fun QuestionRO.toQuestion(): Question {
    return Question(
        id = this._id.toHexString(),
        deWord=this.deWord,
        enMeaning=this.enMeaning,
        enSentence=this.enSentence,
        deSentence=this.deSentence,
        numberOfTries=this.numberOfTries,
        score=this.score,
        isSaved=this.isSaved,
    )
}
fun Question.toQuestionRO():QuestionRO {
    val conv = this
    return QuestionRO().apply {
        _id = if (conv.id.isEmpty()) ObjectId.invoke() else ObjectId(hexString = conv.id)
        deWord=conv.deWord
        enMeaning=conv.enMeaning
        enSentence=conv.enSentence
        deSentence=conv.deSentence
        numberOfTries=conv.numberOfTries
        score=conv.score
        isSaved=conv.isSaved
    }
}