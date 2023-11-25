package com.example.kaustudyroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudyRoomDataViewModel: ViewModel() {
    // StudyRoomFragment
    private val _floor = MutableLiveData<String>()
    private val _studyRoomName = MutableLiveData<String>()
    val floor : LiveData<String> = _floor
    val studyRoomName : LiveData<String> = _studyRoomName
    // LiveData for combined floor and roomName
    val combinedFloorAndRoomName = MediatorLiveData<String>().apply {
        addSource(floor) { floor ->
            val roomName = studyRoomName.value ?: ""
            value = "$floor 스터디룸 $roomName"
        }
        addSource(studyRoomName) { roomName ->
            val floor = floor.value ?: 0  // or a default floor number if null
            value = "$floor 스터디룸 $roomName"
        }
    }

    // StudyRoomTimeTableFragment
    private val _timeSlots = MutableLiveData<List<String>>()
    val timeSlots : LiveData<List<String>> = _timeSlots

    // AdditionalInformationFragment
    private val _userName = MutableLiveData<String>()
    private val _companions = MutableLiveData<List<String>>()
    private val _purposeOfUse = MutableLiveData<String>()
    val userName : LiveData<String> = _userName
    val companions : LiveData<List<String>> = _companions
    val purpose : LiveData<String> = _purposeOfUse

    fun updateRoomDetails(selectedFloor: String, roomName: String) {
        _floor.value = selectedFloor
        _studyRoomName.value = roomName
    }

    fun updateTimeSlots(slots: List<String>) {
        _timeSlots.value = slots
    }

    fun updateUserDetails(name: String, companionsList: List<String>, purpose: String) {
        _userName.value = name
        _companions.value = companionsList
        _purposeOfUse.value = purpose
    }


}