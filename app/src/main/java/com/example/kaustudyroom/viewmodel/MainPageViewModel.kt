package com.example.kaustudyroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kaustudyroom.repository.MainPageRepository
import kotlinx.coroutines.launch

class MainPageViewModel: ViewModel() {

    private val _quote = MutableLiveData<String>()
    val quote : LiveData<String> get() = _quote

    private val repository = MainPageRepository()
    fun retrieveQuote() {
        viewModelScope.launch {
            repository.readQuotes()?.let {adviceResponse ->
                _quote.value = adviceResponse.slip.advice
            }
        }
    }
}