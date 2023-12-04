package com.example.kaustudyroom.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.modelFront.ReservedRoomVO
import com.example.kaustudyroom.modelFront.CheckInVO
import com.example.kaustudyroom.modelFront.ReservePushVO
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

    fun mergeTimeSlots(timeSlots: List<String>): List<String> {
        if (timeSlots.isEmpty()) {
            return emptyList()
        }

        val sortedTimeSlots = timeSlots.sorted()
        val mergedTimeSlots = mutableListOf<String>()

        var currentStartTime = sortedTimeSlots[0].split("-")[0]
        var currentEndTime = sortedTimeSlots[0].split("-")[1]

        for (i in 1 until sortedTimeSlots.size) {
            val nextStartTime = sortedTimeSlots[i].split("-")[0]
            val nextEndTime = sortedTimeSlots[i].split("-")[1]

            if (currentEndTime == nextStartTime) {
                currentEndTime = nextEndTime
            } else {
                mergedTimeSlots.add("$currentStartTime-$currentEndTime")
                currentStartTime = nextStartTime
                currentEndTime = nextEndTime
            }
        }
        mergedTimeSlots.add("$currentStartTime-$currentEndTime")
        return mergedTimeSlots
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

        val pathToCheck = "floor/$floor/$localDate"
        //////////// 데이터베이스 구조 변형 -> 예약 (오늘) 날짜 유무 검사 필요
        databaseReference.child(pathToCheck).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dateSnapshot: DataSnapshot) {
                if (!dateSnapshot.exists()) {
                    println("해당 날짜가 존재 하지 않아서 날짜 데이터 생성")
                    databaseReference.child(pathToCheck).setValue("")
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
        ////////////////



        ///
        //floor노드에 접근
        val floorNode = databaseReference.child("floor").child(floor.toString())
        //room노드에 접근
        val roomNode = floorNode.child(roomName.toString())
        // date노드에 접근
        val dateNode = roomNode.child(localDate.toString())

        for(timeSlot in timeSlots!!){
            //타임슬롯 하나 당 데이터 베이스 push한번씩
            val timeSlotNode = dateNode.child(timeSlot)

            val reservationNode = timeSlotNode
            reservationNode.setValue(
                ReservePushVO(
                    userId,
                    userName,
                    companions,
                    purpose,
                )
            )

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addUserReservedData(){
        val mergedTimeSlots = _timeSlots.value?.let { mergeTimeSlots(it) }
        Log.d("권용현이 만든 mergedTimeSlots","$mergedTimeSlots")
        val pathToCheckTimeList = mutableListOf<String>()
        for (timeslot in mergedTimeSlots!!) {
            Log.d("권용현이 만든 timeSlot :: ","$timeslot")
            // ? ?
            pathToCheckTimeList.add("User/$userId/$localDate/$timeslot")
        }
        println(pathToCheckTimeList)

        val pathToCheck = "User/$userId"
        val pathToCheckDate = "User/$userId/$localDate"
        Log.d("addUserReservedData userId ","${userId}")

        databaseReference.child(pathToCheck).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 해당 UID가 존재하면
                if (dataSnapshot.exists()) {
                    println("해당 UID가 존재")
                    //날짜 검사 -> 날짜 추가
                    databaseReference.child(pathToCheckDate).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dateSnapshot: DataSnapshot) {
                            if (!dateSnapshot.exists()) {
                                println("해당 날짜가 존재 하지 않아서 날짜 데이터 생성")
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
                    ////////////////////////ok
                    //timeslot 검사 -> timeslot 추가
                    for( timeslot in mergedTimeSlots ) {
                        val pathToCheckIn = "User/$userId/$localDate/$timeslot"
                        Log.d("권용현이 pathToCheckIn","$pathToCheckIn")
                        databaseReference.child(pathToCheckIn).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dateSnapshot: DataSnapshot) {
                                if (!dateSnapshot.exists()) {
                                    println("timeslot이 존재 하지 않아서 timeslot 데이터 생성")
                                    databaseReference.child(pathToCheckIn).setValue("")
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                println("오늘 timeslot 데이터가 추가되었습니다.")
                                            } else {
                                                println("오늘 timeslot 데이터 추가 실패: ${task.exception?.message}")
                                            }
                                        }
                                }
                                //val timeSlotNode = dateNode.child(timeSlot)


                                // timeslot 추가 후에 해당 timeslot 밑에 checkInTime과 point 노드 삽입


                            }
                            override fun onCancelled(dateDatabaseError: DatabaseError) {
                                println("timeslot 데이터베이스 읽기 실패: ${dateDatabaseError.message}")
                            }
                        })
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("데이터베이스 읽기 실패: ${databaseError.message}")
            }
        })

        for( timeslot in mergedTimeSlots ) {
            // User노드에 접근
            val userNode = databaseReference.child("User").child(userId)
            // date노드에 접근
            val dateNode = userNode.child(localDate.toString())
            // timeslot노드에 접근
            val timeNode = dateNode.child(timeslot)

            val checkedInTime = ""
            val point= 1
            val checkInNPointNode = timeNode
            checkInNPointNode.setValue(
                CheckInVO(
                    checkedInTime,
                    point
                )
            )
        }
        Log.d("misson 데이터베이스 넣기","ㅇㅋㅇㅋ")
    }






}