package com.example.kaustudyroom.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class StudyRoomDataViewModel: ViewModel() {
    // StudyRoomFragment
    private val authViewModel = AuthViewModel()

    val userId: String
        get() = authViewModel.getUserIdDirectly()



    private val _floor = MutableLiveData<Int>()
    private val _studyRoomName = MutableLiveData<String>()

    val floor : LiveData<Int> = _floor
    val studyRoomName : LiveData<String> = _studyRoomName
    // LiveData for combined floor and roomName
    val combinedFloorAndRoomName = MediatorLiveData<String>().apply {
        addSource(floor) { floor ->
            val roomName = studyRoomName.value ?: ""
            value = "$floor 층 스터디룸 $roomName"
        }
        addSource(studyRoomName) { roomName ->
            val floor = floor.value ?: 0  // or a default floor number if null
            value = "$floor 층 스터디룸 $roomName"
        }
    }

    // StudyRoomTimeTableFragment
    private val _timeSlots = MutableLiveData<List<String>>()
    val timeSlots : LiveData<List<String>> = _timeSlots

    // AdditionalInformationFragment
    private val _userName = MutableLiveData<String>()
    private val _companions = MutableLiveData<String>()
    private val _purposeOfUse = MutableLiveData<String>()
    val userName : LiveData<String> = _userName
    val companions : LiveData<String> = _companions
    val purpose : LiveData<String> = _purposeOfUse

    fun updateRoomDetails(selectedFloor: Int, roomName: String) {
        _floor.value = selectedFloor
        _studyRoomName.value = roomName
    }

    fun updateTimeSlots(slots: List<String>) {
        _timeSlots.value = slots
    }

    fun updateUserDetails(name: String, companions: String, purpose: String) {
        _userName.value = name
        _companions.value = companions
        _purposeOfUse.value = purpose
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun addReserveData(){
        val localDate: LocalDate = LocalDate.now()
        val timeSlots = _timeSlots.value
        val floor = _floor.value?.toInt()
        val roomName = _studyRoomName.value.toString()
        val userName = _userName.value.toString()
        val companions = _companions.value.toString()
        val purpose = _purposeOfUse.value.toString()
        val userId = userId

        val databaseReference = FirebaseDatabase.getInstance().reference

        //floor노드에 접근
        val floorNode = databaseReference.child("floor").child(floor.toString())
        //room노드에 접근
        val roomNode = floorNode.child(roomName.toString())




        for(timeSlot in timeSlots!!){
            //타임슬롯 하나 당 데이터 베이스 push한번씩
            val reservationNode = roomNode.push()
            reservationNode.setValue(
                ReservedRoomVO(
                    userId,
                    userName,
                    companions,
                    purpose,
                    timeSlot,
                    localDate.toString(),
                    roomName,
                    floor!!
                )
            )



//            Log.d("addReserveData ! ","${timeSlot} 보낼거야 !! ")
//            Log.d("addReserveData"," ${floor} ${roomName} ${userName} ${companions} ${purpose} ${timeSlot}")
//            Log.d("today is ", "${localDate}")
//            Log.d("userId is ", "${userId}")

        }

    }






}