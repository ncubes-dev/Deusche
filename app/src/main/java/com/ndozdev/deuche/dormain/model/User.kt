package com.ndozdev.deuche.dormain.model

data class User(
    var name:String?=null,
    var gender: String = "",
    var points: Int = 0,
    var streak: Int = 1,
)
