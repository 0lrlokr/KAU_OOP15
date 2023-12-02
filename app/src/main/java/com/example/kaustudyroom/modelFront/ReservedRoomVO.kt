package com.example.kaustudyroom.modelFront

data class ReservedRoomVO(
    val userId: String,
    val userName: String?,
    val companions: String,
    val purpose: String,
    val timeSlot:String,
    val date: String,
    val room:String,
    val floor:Int

)
