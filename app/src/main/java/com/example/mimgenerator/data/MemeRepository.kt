package com.example.mimgenerator.data

import android.content.Context
import android.graphics.BitmapFactory
import com.example.mimgenerator.data.remote.RemoteDataSource
import com.example.mimgenerator.data.remote.network.ApiResponse
import com.example.mimgenerator.data.remote.response.Data
import com.example.mimgenerator.domain.model.Meme
import com.example.mimgenerator.domain.repository.IMemeRepository
import com.example.mimgenerator.utils.DataMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class MemeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    @ApplicationContext private val context: Context
): IMemeRepository{
    override fun getMeme(): Flow<Resource<List<Meme>>> =
        object:NetworkBoundResource<List<Meme>, Data?>(), Flow<Resource<Data?>>{
            override suspend fun collect(collector: FlowCollector<Resource<Data?>>) {
                TODO("Not yet implemented")
            }

            override fun loadFromNetwork(data: Data?): Flow<List<Meme>> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<Data?>> {
                return remoteDataSource.getMeme()
            }

        }.asFlow()

}