package com.example.myapplication.network

import com.example.myapplication.Rom
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RomApi {
    @GET("posts")
    suspend fun getRoms(): List<Rom>
}

object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: RomApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RomApi::class.java)
    }
}