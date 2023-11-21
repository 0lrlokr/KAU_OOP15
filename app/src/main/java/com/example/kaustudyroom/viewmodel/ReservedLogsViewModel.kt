package com.example.kaustudyroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReservedLogsViewModel:ViewModel() {
    private val _reservationLog = MutableLiveData<ReservedRoomVO>()




}