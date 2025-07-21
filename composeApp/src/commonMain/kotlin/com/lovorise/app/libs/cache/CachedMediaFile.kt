package com.lovorise.app.libs.cache

import coil3.PlatformContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.use
import org.kotlincrypto.hash.sha2.SHA256


const val MEDIA_CACHE_BUFFER_SIZE = 8192L // 8 KB is a good standard size


fun ByteArray.toHexString(): String {
    val result = StringBuilder(this.size * 2)
    for (byte in this) {
        val hex = byte.toInt() and 0xFF
        if (hex < 0x10) result.append('0') // pad single-digit hex values
        result.append(hex.toString(16))
    }
    return result.toString()
}

fun String.toSha256Hash(): String {
    val digest = SHA256()
    val inputBytes = this.encodeToByteArray()
    val hash = digest.digest(inputBytes)
    // Convert to hex string
    return hash.toHexString()
}

suspend fun downloadMediaFile(url: String): ByteReadChannel?{
    return HttpClient().use { client ->
        try {
            val response: HttpResponse = client.get(url)
            response.body<ByteReadChannel>()
        }catch (e:Exception){
            null
        }finally {
            client.close()
        }
    }
}

expect class MediaCacheManager(context:PlatformContext) {
    suspend fun getMediaPathIfExists(url: String): String?
    suspend fun downloadAndCache(url: String): String?
    suspend fun downloadAndCacheIfRequired(url: String): String?
}
