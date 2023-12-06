package com.example.kaustudyroom.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kaustudyroom.pages.PointNCamera.Point
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PointRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkTimeSlotAndNavigate(userId: String): Boolean = withContext(Dispatchers.IO) {
        val currentDate: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentTime: String = LocalTime.now().format(formatter) // ex) 11:30
        val currentHour: String = currentTime.slice(0 until 2) // ex) 11
        val currentMinute: String = currentTime.slice(3 until 5) // ex) 30

        // 시간 선택은 최대 3개까지 이고 30분 이내로 사진을 촬영해야 하기 때문에 시작시간의 +3까지만 비교
        for (i in 1 until 4) {
            val startTime: String = currentHour
            val endTime: String = if ((currentHour.toInt() + i) >= 10) {(currentHour.toInt() + i).toString()}
            else { "0"+(currentHour.toInt() + i).toString()}

            val currentTimeSlot = "$startTime-$endTime"
            try {
                val snapshot = databaseReference.child("User/$userId/$currentDate/$currentTimeSlot").get().await()
                // firebase에 현재 시각에 해당하는 timeslot이 존재하면서 현재 분이 30분 이하일 경우에만 true
                if (snapshot.exists() && currentMinute.toInt() <= 30) {
                    return@withContext true
                }
            } catch (e: Exception) {
                println("Error checking time slot: $e")
            }
        }
        return@withContext false
    }

    fun getPoints(userId: String): LiveData<List<Point>> {
        val mutableLiveData = MutableLiveData<List<Point>>()

        // firebase의 User테이블의 해당 유저의 하위 UID테이블에서 벌점 관련 데이터 가져오기
        val pointDBRef = databaseReference.child("User").child(userId)
        pointDBRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pointList = mutableListOf<Point>()
                val currentDateTime = LocalDateTime.now()

                for (userSnapshot in dataSnapshot.children) {
                    val date = userSnapshot.key.toString()

                    for (dateSnapshot in userSnapshot.children) {
                        val point = dateSnapshot.child("point").value.toString().toInt()
                        val timeslot = dateSnapshot.key.toString()
                        val startTime = timeslot.slice(0 until 2)
                        val timeslotDateTime =
                            LocalDateTime.parse("$date $startTime", DateTimeFormatter.ofPattern("yyyy-MM-dd HH"))

                        // 벌점이 존재하면서 미래시간을 제외한 데이터들만 조회
                        if (point > 0 && !timeslotDateTime.isAfter(currentDateTime)) {
                            val floor = dateSnapshot.child("floor").value
                            val room = dateSnapshot.child("room").value
                            val roomName = floor.toString() + "층 스터디룸 $room"
                            val pointListSet = Point(date, roomName, point, timeslot)
                            pointList.add(pointListSet)
                        }
                    }
                }
                mutableLiveData.value = pointList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        })

        return mutableLiveData
    }
}