package com.example.mimgenerator.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

import android.widget.FrameLayout


import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ThreadLocalRandom

object Utils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Untuk API level 23 atau di atasnya (Android 6.0+), gunakan NetworkCapabilities
                val network = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            } else {
                // Untuk API level sebelumnya, gunakan deprecated methods
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo != null && networkInfo.isConnected
            }
        }

        return false
    }


    fun saveLayoutAsImage(frameLayout: FrameLayout): Bitmap {
        frameLayout.clearFocus()
        val bitmap =
            Bitmap.createBitmap(frameLayout.width, frameLayout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        frameLayout.draw(canvas)
        return bitmap
    }

    // Fungsi untuk menyimpan bitmap ke storage dan mendapatkan Uri
    fun saveImageToExternalStorage(context: Context, bitmap: Bitmap, filename: String): Uri? {
        // Tentukan lokasi penyimpanan dengan menggunakan context.getExternalFilesDir
        val imagesFolder =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages")
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs() // Buat direktori jika belum ada
        }
        val file = File(imagesFolder, "$filename.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        // Dapatkan dan kembalikan Uri dari FileProvider
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }

    fun randomString(length: Int): String {
        val random = ThreadLocalRandom.current()
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { alphabet[random.nextInt(alphabet.length)] }
            .joinToString("")
    }

}