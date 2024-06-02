package com.ndozdev.deuche.presentation

import com.ndozdev.deuche.data.Question


sealed class MainScreenEvents {
    data class UpdateQuestion(val question: Question) : MainScreenEvents()
    data class NextQuestion(val index:Int) : MainScreenEvents()
    data class UpdateSpeed(val speed:Float) : MainScreenEvents()
    data class FilterQuestion(val selected:String) : MainScreenEvents()


}