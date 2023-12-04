package com.example.kaustudyroom.modelFront

data class ReservedRoomVO(
    val userName: String?,
    val companions: String,
    val purpose: String,
    val timeSlot:String,
    val date: String,
    val room:String,
    val floor: String
)

data class ReservePushVO(
    val userId: String,
    val userName: String?,
)

data class CheckInVO(
    val userName: String?,
    val companions: String,
    val purpose: String,
    val room:String,
    val floor: String,
    val checkInTime: String,
    val point: Int
)
