package com.example.mimgenerator.domain.usecase

import com.example.mimgenerator.data.Resource
import com.example.mimgenerator.domain.model.Meme
import kotlinx.coroutines.flow.Flow

interface MemeUseCase {
    fun getMeme(): Flow<Resource<List<Meme>>>
}