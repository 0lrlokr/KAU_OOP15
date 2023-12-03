package com.example.kaustudyroom.repository

import androidx.lifecycle.ViewModel
import com.example.kaustudyroom.AdviceResponse
import com.example.kaustudyroom.adviceApi


class MainPageRepository: ViewModel() {
    suspend fun readQuotes(): AdviceResponse? {
        return try {
            adviceApi.getAdvices()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}