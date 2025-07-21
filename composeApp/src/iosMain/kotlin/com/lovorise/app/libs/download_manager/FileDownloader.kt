package com.lovorise.app.libs.download_manager

import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.NSUserDomainMask
import platform.Foundation.downloadTaskWithRequest

@OptIn(ExperimentalForeignApi::class)
actual suspend fun downloadFile(context: PlatformContext, url: String, fileName: String,fromNetwork:Boolean,onSuccess:()->Unit,onError:()->Unit){
    withContext(Dispatchers.IO) {
        val nsUrl = NSURL.URLWithString(url) ?: return@withContext onError()
        val request = NSURLRequest.requestWithURL(nsUrl)
        val session = NSURLSession.sharedSession
        val task = session.downloadTaskWithRequest(request) { tempFile, response, error ->
            if (error != null || tempFile == null) {
                return@downloadTaskWithRequest onError()
            }
            val fileManager = NSFileManager.defaultManager
            val documentsDirectory = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory, NSUserDomainMask, true
            ).firstOrNull() as? String ?: run {
                return@downloadTaskWithRequest onError()
            }
            val destinationPath = "$documentsDirectory/$fileName"
            val destinationUrl = NSURL.fileURLWithPath(destinationPath)
            try {
                fileManager.moveItemAtURL(tempFile, destinationUrl, null)
                return@downloadTaskWithRequest onSuccess()
            } catch (e: Exception) {
                return@downloadTaskWithRequest onError()
            }
        }
        task.resume()
    }
}
