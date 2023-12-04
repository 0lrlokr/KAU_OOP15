package com.example.kaustudyroom.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaustudyroom.repository.PointRepository
import kotlinx.coroutines.launch

class PointViewModel: ViewModel() {
    private val repository = PointRepository()

    private var _navigateToCameraFragment = MutableLiveData<Boolean>().apply { value = false }
    val navigateToCameraFragment: LiveData<Boolean> = _navigateToCameraFragment

    private val _showToastEvent = MutableLiveData<String>()
    val showToastEvent: LiveData<String> get() = _showToastEvent
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkTimeSlotAndNavigate(userId: String) {

        viewModelScope.launch {
            val timeSlotExits = repository.checkTimeSlotAndNavigate(userId)

            if (timeSlotExits) {
                _navigateToCameraFragment.value = true
            } else {
                _showToastEvent.value = "입실 시간이 아닙니다."
            }
        }
    }
}