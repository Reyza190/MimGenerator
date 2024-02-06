package com.example.mimgenerator.di


import com.example.mimgenerator.data.MemeRepository
import com.example.mimgenerator.domain.usecase.MemeInteractor
import com.example.mimgenerator.domain.usecase.MemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsUseCase(memeRepository: MemeRepository): MemeUseCase=MemeInteractor(memeRepository)
}