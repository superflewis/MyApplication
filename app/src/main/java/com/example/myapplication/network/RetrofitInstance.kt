package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Retrofit API interface
interface RomApi {
    @GET("{path}")
    suspend fun getRoms(@Path("path") path: String): List<Rom>
}

// Singleton Retrofit instance
object RetrofitInstance {
    private const val BASE_URL = "https://example.com/" // Placeholder; update later

    val api: RomApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RomApi::class.java)
    }
}