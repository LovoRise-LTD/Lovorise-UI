package com.lovorise.app.libs.cache

import coil3.PlatformContext
import io.ktor.utils.io.readRemaining
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fwrite
import kotlin.OptIn
import kotlin.String
import kotlin.error

actual class MediaCacheManager actual constructor(private val context: PlatformContext) {

    @OptIn(ExperimentalForeignApi::class)
    private fun cacheDir(): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true)
            .filterIsInstance<String>()

        val baseCacheDir = paths.firstOrNull()
            ?: error("Failed to get cache directory")

        val mediaCacheDir = "$baseCacheDir/media"

        NSFileManager.defaultManager.createDirectoryAtPath(
            path = mediaCacheDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
        return mediaCacheDir
    }

    private fun getFileForUrl(url: String): String {
        val name = url.toSha256Hash() + "." + url.substringAfterLast('.', "bin")
        return "${cacheDir()}/$name"
    }

    actual suspend fun getMediaPathIfExists(url: String): String? {
        val path = getFileForUrl(url)
        return if (NSFileManager.defaultManager.fileExistsAtPath(path)) path else null
    }

    actual suspend fun downloadAndCacheIfRequired(url: String) : String?{
        return getMediaPathIfExists(url) ?: downloadAndCache(url)
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun downloadAndCache(url: String): String? {
        val path = getFileForUrl(url)
        val channel = downloadMediaFile( url) ?: return  null

        withContext(Dispatchers.Default) {
            val fileHandle = fopen(path, "w+b") ?: error("Cannot open file at $path")
            try {
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(8192)
                    while (!packet.exhausted()) {
                        val bytes = packet.readByteArray()
                        fwrite(bytes.refTo(0), 1.convert(), bytes.size.convert(), fileHandle)
                    }
                }
            } finally {
                fclose(fileHandle)
            }
        }

        return path
    }
}
