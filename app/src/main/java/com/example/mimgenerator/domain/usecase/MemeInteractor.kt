package com.example.mimgenerator.domain.usecase

import com.example.mimgenerator.data.Resource
import com.example.mimgenerator.domain.model.Meme
import com.example.mimgenerator.domain.repository.IMemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MemeInteractor @Inject constructor(private val memeRepository: IMemeRepository): MemeUseCase {
    override fun getMeme(): Flow<Resource<List<Meme>>> {
        return memeRepository.getMeme()
    }


}