package com.example.kaustudyroom.pages.PointNCamera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _latestImageUrl = MutableLiveData<String>()
    val latestImageUrl: LiveData<String> = _latestImageUrl

    fun setLatestImageUrl(url: String) {
        _latestImageUrl.value = url
    }
}