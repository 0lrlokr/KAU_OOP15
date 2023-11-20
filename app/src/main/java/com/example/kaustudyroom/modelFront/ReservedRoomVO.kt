package com.example.kaustudyroom.modelFront

import com.example.kaustudyroom.pages.StudyRoomTimeTable.Estate

data class ReservedRoomVO (
    val id:Int ,
    val userId: Int,
    val time:String,
    val room : String,
    val companion : String,
    val purpose: String,
)
