package com.lovorise.app.libs.cache

import coil3.PlatformContext
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import java.io.File

actual class MediaCacheManager actual constructor(private val context: PlatformContext) {


    private fun cacheDir(): File = File(context.cacheDir, "media").also { it.mkdirs() }

    private fun getFileForUrl(url: String): File {
        val name = url.toSha256Hash() + "." + url.substringAfterLast('.', "bin")
        return File(cacheDir(), name)
    }

    actual suspend fun getMediaPathIfExists(url: String): String? {
        val file = getFileForUrl(url)
        return if (file.exists()) file.absolutePath else null
    }

    actual suspend fun downloadAndCacheIfRequired(url: String) : String?{
        return getMediaPathIfExists(url) ?: downloadAndCache(url)
    }

    actual suspend fun downloadAndCache(url: String): String? {
        val file = getFileForUrl(url)
        val channel = downloadMediaFile(url) ?: return null

        withContext(Dispatchers.IO) {
            file.outputStream().use { output ->
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(8192)
                    while (!packet.exhausted()) {
                        val bytes = packet.readByteArray()
                        output.write(bytes)
                    }
                }
            }
        }

        return file.absolutePath
    }
}


