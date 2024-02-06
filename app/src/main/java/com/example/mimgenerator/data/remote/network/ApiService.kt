package com.example.mimgenerator.data.remote.network

import com.example.mimgenerator.data.remote.response.ListMemeResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("get_memes")
    suspend fun getMeme(): ListMemeResponse
}