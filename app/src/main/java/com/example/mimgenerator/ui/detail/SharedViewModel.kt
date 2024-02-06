package com.example.mimgenerator.ui.detail

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimgenerator.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val context: Application
) : ViewModel() {
    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    private val _downloadStatus = MutableLiveData<Resource<String>>()
    val downloadStatus: LiveData<Resource<String>> = _downloadStatus

    fun downloadAndSaveImage(imageUrl: Bitmap, title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _downloadStatus.postValue(Resource.Loading())
                val result = saveImageToGallery(context, imageUrl, title, description)
                if (result != null) {
                    _downloadStatus.postValue(Resource.Success("Sukses"))
                }
            } catch (e: Exception) {
                _downloadStatus.postValue(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    private fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap,
        title: String,
        description: String
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // Catatan: Untuk Android Q dan yang lebih baru, tidak perlu menentukan DIRECTORY_PICTURES karena scoped storage
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            put(MediaStore.Images.Media.DESCRIPTION, description)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)) {
                        throw IOException("Failed to save bitmap.")
                    }
                }
            } ?: throw IOException("Failed to create new MediaStore record.")
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return uri
    }
}