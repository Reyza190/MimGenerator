package com.example.mimgenerator.utils

import com.example.mimgenerator.data.remote.response.Data
import com.example.mimgenerator.domain.model.Meme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


object DataMapper {
    fun mapResponseToDomain(result: Data?): Flow<List<Meme>> {
        val memeList = ArrayList<Meme>()
        result?.memes?.map {
            val meme = Meme(
                id = it?.id!!,
                url =it.url!!,
                width = it.width!!,
                height = it.height!!,
                name = it.name!!
            )
            memeList.add(meme)
        }
        return flowOf(memeList)
    }
}