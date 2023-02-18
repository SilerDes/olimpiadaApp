package com.kazbekov.olimpiada.network

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DownloadHandler private constructor(
    private val context: Context,
    private val uri: Uri,
) {

    private var downloadId: Long = 0
    private var downloadManager: DownloadManager? = null
    private var lastFile: File? = null

    /**
     *
     * Выполняет запрос на скачивание файла, сохраняет downloadId для дальнейшего ослеживания загрузки
     *
     * */
    suspend fun invoke() {
        withContext(Dispatchers.IO) {
            val externalStorageDir = context.getExternalFilesDir("")
                ?: throw Exception("Не удалось получить доступ к external storage")

            lastFile = File(
                externalStorageDir,
                Repository.DESTINATION_FILE_NAME
            )

            val request = DownloadManager.Request(uri)
                .setDestinationUri(Uri.fromFile(lastFile))

            downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            downloadId = downloadManager!!.enqueue(request)
        }
    }

    /**
     *
     * Дожидается, когда загрузка закончится и возвращает true, если загрузка завершена успешно
     * false, если загрузка не удалась
     *
     * */
    suspend fun await(): Boolean {
        return withContext(Dispatchers.IO) {
            val isFinished: Boolean

            mainLoop@ while (true) {
                val cursor =
                    downloadManager!!.query(DownloadManager.Query().setFilterById(downloadId))
                if (cursor.moveToFirst()) {
                    val columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val status =
                        cursor.getInt(if (columnStatusIndex == -1) continue else columnStatusIndex)

                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                            isFinished = false
                            break@mainLoop
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isFinished = true
                            break@mainLoop
                        }
                        DownloadManager.STATUS_PAUSED -> {
                            isFinished = false
                            break@mainLoop
                        }
                        else -> {
                            continue@mainLoop
                        }
                    }
                }
            }

            if (!isFinished) {
                lastFile?.delete() //Удаляем файл, если загрузка не былауспешно завершена
            }


            return@withContext isFinished
        }
    }

    class Builder {
        private var context: Context? = null
        private var uri: Uri? = null


        fun attach(context: Context): Builder {
            this.context = context
            return this
        }

        fun setUrl(url: String): Builder {
            this.uri = Uri.parse(url)
            return this
        }

        fun build(): DownloadHandler {
            return DownloadHandler(
                context = context!!,
                uri = uri!!,
            )
        }
    }
}
