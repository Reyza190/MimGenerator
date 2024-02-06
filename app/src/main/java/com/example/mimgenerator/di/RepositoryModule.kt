package com.example.mimgenerator.di

import com.example.mimgenerator.data.MemeRepository
import com.example.mimgenerator.domain.repository.IMemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(memeRepository: MemeRepository): IMemeRepository

}