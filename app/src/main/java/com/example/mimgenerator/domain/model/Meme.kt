package com.example.mimgenerator.domain.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize

@SuppressLint("ParcelCreator")
@VersionedParcelize
data class Meme(
    val id: String,
    val name: String,
    val width: Int,
    val height:Int,
    val url: String
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }
}
