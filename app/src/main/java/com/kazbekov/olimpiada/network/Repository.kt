package com.kazbekov.olimpiada.network

import android.content.Context
import android.util.Log
import com.kazbekov.olimpiada.data.ServiceVK
import com.kazbekov.olimpiada.data.ServiceVKWrapper
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Repository(private val context: Context) {

    private val serviceVKJsonAdapter = Moshi.Builder().build().adapter(ServiceVKWrapper::class.java)

    //Вернет true - если файл надо скачать, false в ином случае
    suspend fun needDownload(): Boolean = withContext(Dispatchers.IO) {
        return@withContext !File(context.getExternalFilesDir(""), DESTINATION_FILE_NAME).exists()
    }

    //Вернет true если файл успешно скачан
    suspend fun downloadFile(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val downloadHandler = DownloadHandler.Builder()
                .attach(context)
                .setUrl(BASE_LINK)
                .build()

            downloadHandler.invoke() //Делает запрос на скачивание, может бросить исключение
            downloadHandler.await() //Ожидает завершения загрузки, возвращает Boolean, может бросить исключение

        } catch (t: Throwable) {
            false
        }
    }

    suspend fun parseServices(): List<ServiceVK> = withContext(Dispatchers.IO) {
        val servicesJson = File(context.getExternalFilesDir(""), DESTINATION_FILE_NAME).readText()

        return@withContext serviceVKJsonAdapter.fromJson(servicesJson)?.items ?: emptyList()
    }

    companion object {
        private const val BASE_LINK =
            "https://mobile-olympiad-trajectory.hb.bizmrg.com/semi-final-data.json"
        const val DESTINATION_FILE_NAME = "olimp.txt"
    }
}