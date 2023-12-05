package com.example.kaustudyroom.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class PointRepository {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkTimeSlotAndNavigate(userId: String): Boolean = withContext(Dispatchers.IO) {
        val currentDate: LocalDate = LocalDate.now()
        println("currentDate: $currentDate")
        val formatter = DateTimeFormatter.ofPattern("HH")
        val currentTime: String = LocalTime.now().format(formatter)
        println("Formatted time: $currentTime")

        for (i in 1 until 4) {
            val startTime: String = currentTime
            val endTime: String = if ((currentTime.toInt() + i) >= 10) {(currentTime.toInt() + i).toString()}
            else { "0"+(currentTime.toInt() + i).toString()}
            val currentTimeSlot: String = "$startTime-$endTime"
            println("Checking for currentTimeSlot: $currentTimeSlot")

            try {
                val snapshot = databaseReference.child("User/$userId/$currentDate/$currentTimeSlot").get().await()
                if (snapshot.exists()) {
                    return@withContext true
                }
            } catch (e: Exception) {
                println("Error checking time slot: $e")
            }
        }
        return@withContext false
    }
}