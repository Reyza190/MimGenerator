package com.example.mimgenerator.domain.repository

import com.example.mimgenerator.data.Resource
import com.example.mimgenerator.domain.model.Meme
import kotlinx.coroutines.flow.Flow

interface IMemeRepository {
    fun getMeme(): Flow<Resource<List<Meme>>>

}