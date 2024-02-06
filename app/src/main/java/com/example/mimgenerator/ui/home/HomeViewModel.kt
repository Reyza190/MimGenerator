package com.example.mimgenerator.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mimgenerator.domain.usecase.MemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(memeUseCase: MemeUseCase) : ViewModel() {
    val meme = memeUseCase.getMeme().asLiveData()
}