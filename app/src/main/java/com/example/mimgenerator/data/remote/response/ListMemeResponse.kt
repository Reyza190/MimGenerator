package com.example.mimgenerator.data.remote.response

import com.google.gson.annotations.SerializedName

data class ListMemeResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class MemesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("box_count")
	val boxCount: Int? = null
)

data class Data(

	@field:SerializedName("memes")
	val memes: List<MemesItem?>? = null
)
