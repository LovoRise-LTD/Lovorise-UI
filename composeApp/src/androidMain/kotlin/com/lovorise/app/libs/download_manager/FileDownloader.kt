package com.lovorise.app.libs.download_manager

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import coil3.PlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

actual suspend fun downloadFile(context: PlatformContext, url: String, fileName: String,fromNetwork:Boolean,onSuccess:()->Unit,onError:()->Unit){
    if (fromNetwork) downloadOverNetwork(context, url, fileName,onSuccess, onError)
    else downloadLocalFile(context,url,fileName, mimeType = if (fileName.contains("png")) "image/png" else "video/mp4" ,onSuccess, onError)
}

private suspend fun downloadLocalFile(context: Context, url: String, fileName:String, mimeType:String,onSuccess:()->Unit,onError:()->Unit) {
    withContext(Dispatchers.IO) {
        val stream: InputStream = FileInputStream(File(url))
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = context.contentResolver

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        )
        if (uri != null) {
            try {
                stream.use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                onSuccess()
                println("the success callback invoked!!")
            } catch (e: Exception) {
                onError()
                e.printStackTrace()
            } finally {
                stream.close()
            }
        }
    }


}


private suspend fun downloadOverNetwork(
    context: PlatformContext,
    url: String,
    fileName: String,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri).apply {
                setTitle("Downloading $fileName")
                setDescription("Please wait while the file is being downloaded.")
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//                setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            }
            val downloadId = downloadManager.enqueue(request)

            val query = DownloadManager.Query().setFilterById(downloadId)
            var isDownloading = true
            while (isDownloading) {
                val cursor = downloadManager.query(query)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val status =
                            it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            println("the downloading file success")
                            isDownloading = false
                            onSuccess()
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            isDownloading = false
                            onError()
                        }
                    }
                }
                Thread.sleep(500) // Polling interval
            }
        } catch (e: Exception) {
            onError()
            e.printStackTrace()
        }
    }
}
