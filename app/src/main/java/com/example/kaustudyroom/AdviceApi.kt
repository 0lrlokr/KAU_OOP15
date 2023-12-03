package com.example.kaustudyroom

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AdviceApi {
    @GET("advice")
    suspend fun getAdvices(): AdviceResponse
}

data class AdviceResponse(
    val slip: Slip
)

data class Slip(
    val id: Int,
    val advice: String
)

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.adviceslip.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val adviceApi = retrofit.create(AdviceApi::class.java)