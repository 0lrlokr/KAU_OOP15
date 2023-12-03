package com.example.kaustudyroom.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate

class StudyRoomDataViewModel: ViewModel() {
    val databaseReference = FirebaseDatabase.getInstance().reference

    @RequiresApi(Build.VERSION_CODES.O)
    val localDate: LocalDate = LocalDate.now()
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
        val localDate = localDate
        val timeSlots = _timeSlots.value
        val floor = _floor.value?.toInt()
        val roomName = _studyRoomName.value.toString()
        val userName = _userName.value.toString()
        val companions = _companions.value.toString()
        val purpose = _purposeOfUse.value.toString()
        val userId = userId

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

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addUserReservedData(){
        val pathToCheck = "User/$userId"
        val pathToCheckDate = "User/$userId/$localDate"
        Log.d("addUserReservedData userId ","${userId}")

        databaseReference.child(pathToCheck).addListenerForSingleValueEvent(object :
            ValueEventListener {
            //해당 uid 존재하지 않으면
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    println("해당 UID가 존재")
                    //날짜 검사 -> 날짜 추가
                    ////////////////////
                    databaseReference.child(pathToCheckDate).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dateSnapshot: DataSnapshot) {
                            if (dateSnapshot.exists()) {
                                println("오늘 날짜에 이미 데이터가 존재합니다.")
                                // 이미 오늘 날짜에 데이터가 존재하므로 추가 작업 필요 없음
                            } else {
                                println("오늘 날짜에 데이터가 존재하지 않음")
                                // 바로 오늘 날짜 추가
                                databaseReference.child(pathToCheckDate).setValue("")
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            println("오늘 날짜 데이터가 추가되었습니다.")
                                        } else {
                                            println("오늘 날짜 데이터 추가 실패: ${task.exception?.message}")
                                        }
                                    }
                            }
                        }

                        override fun onCancelled(dateDatabaseError: DatabaseError) {
                            println("날짜 데이터베이스 읽기 실패: ${dateDatabaseError.message}")
                        }
                    })
                    ///////////



                } else {
                    println("해당 UID 존재하지 않음")
                    //바로 오늘 날짜 추가
                    val userPath = "User/$userId"
                    databaseReference.child(userPath).setValue("")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("새로운 사용자 데이터가 추가되었습니다.")
                            } else {
                                println("사용자 데이터 추가 실패: ${task.exception?.message}")
                            }
                        }

                    databaseReference.child(pathToCheckDate).setValue("")
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                println("오늘 날짜가 추가되었습니다. ")
                            }else{
                                println("날짜 생성 실패 : ${task.exception?.message}")
                            }
                        }


                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("데이터베이스 읽기 실패: ${databaseError.message}")
            }
        })


        Log.d("misson 데이터베이스 넣기","ㅇㅋㅇㅋ")




    }






}