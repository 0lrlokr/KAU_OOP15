package com.example.kaustudyroom.modelFront

import com.example.kaustudyroom.pages.StudyRoomTimeTable.Estate

data class ReservedRoomVO (
    val log_id:Int ,
    val user_uid: String,
    val time_slot:String,
    val floor:Int,
    val room : String,
    val companion : String,
    val purpose: String,
)
