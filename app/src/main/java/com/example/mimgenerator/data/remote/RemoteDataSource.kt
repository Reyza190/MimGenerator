package com.example.mimgenerator.data.remote

import android.util.Log
import com.example.mimgenerator.data.remote.network.ApiResponse
import com.example.mimgenerator.data.remote.network.ApiService
import com.example.mimgenerator.data.remote.response.Data
import com.example.mimgenerator.data.remote.response.MemesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getMeme(): Flow<ApiResponse<Data?>> {
        return flow {
            try {
                val response = apiService.getMeme()
                val dataArray = response.success
                if (dataArray == true) {
                    emit(ApiResponse.Success(response.data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}