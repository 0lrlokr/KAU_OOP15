package com.example.kaustudyroom


enum class Estate {
    Available, // 예약가능
    InUse,   // 사용중
    NotAvailable // 예약불가
}
data class StudyroomTimeTable (val time:String,
                               val id:Int ,
                               val state: Estate)