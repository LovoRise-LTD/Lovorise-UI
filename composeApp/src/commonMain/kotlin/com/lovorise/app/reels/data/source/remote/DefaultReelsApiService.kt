package com.lovorise.app.reels.data.source.remote

import com.lovorise.app.reels.data.source.remote.dto.CreateReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.CreateUpdateReelResponseDto
import com.lovorise.app.reels.data.source.remote.dto.DeleteReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.FavouritesReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.FilterReelsRequestDto
import com.lovorise.app.reels.data.source.remote.dto.MarkReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.MyReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.PostReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.ReelSignedUrlRequestDto
import com.lovorise.app.reels.data.source.remote.dto.ReelSignedUrlResponseDto
import com.lovorise.app.reels.data.source.remote.dto.ReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.ReportReelDto
import com.lovorise.app.reels.data.source.remote.dto.UpdateReelRequestDto
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer

class DefaultReelsApiService(
    private val httpClient: HttpClient
) : ReelsApiService {

    override suspend fun getReelsForYou(token: String, limit: Int,page:Int) : ReelsResponseDto? {
        val response: ReelsResponseDto? = try {
            httpClient.get("$REELS_FOR_YOU?limit=$limit&page=$page") {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }

        return response
    }

    override suspend fun getReelsFromMatches(token: String, limit: Int,page:Int) : ReelsResponseDto? {
        val response: ReelsResponseDto? = try {
            httpClient.get("$REELS_FROM_MATCHES?limit=$limit&page=$page") {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }

        return response
    }

    override suspend fun filterReels(token: String, limit: Int, data: FilterReelsRequestDto,page:Int) : ReelsResponseDto? {
        val response: ReelsResponseDto? = try { httpClient.post("$FILTER_REELS?limit=$limit&page=$page") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
            null
        }

        return response
    }

    override suspend fun getFavoriteReels(token: String, limit: Int,page:Int) : FavouritesReelsResponseDto? {
        val response: FavouritesReelsResponseDto? = try{httpClient.get("$FAVORITE_REELS?limit=$limit&page=$page") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){
        null
    }

        return response
    }

    override suspend fun getMyReels(token: String,limit:Int,page:Int): MyReelsResponseDto? {
        val response: MyReelsResponseDto? = try{httpClient.get("$MY_REELS?limit=$limit&page=$page") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }.body()}catch (e:Exception){
            null
        }

        return response
    }

    override suspend fun getPresignedUrl(token: String, data: ReelSignedUrlRequestDto): ReelSignedUrlResponseDto? {
        val response: ReelSignedUrlResponseDto? = try{httpClient.post(CREATE_PRESIGNED_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
            null
        }

        return response
    }

    override suspend fun createReel(token: String, data: CreateReelRequestDto): CreateUpdateReelResponseDto? {
        val response: CreateUpdateReelResponseDto? = try{httpClient.post(REELS_BASE_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()
        }catch (e:Exception){
            null
        }

        return response
    }

    override suspend fun updateReel(token: String, data: UpdateReelRequestDto): CreateUpdateReelResponseDto? {
        val response: CreateUpdateReelResponseDto? = try{httpClient.put(REELS_BASE_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }.body()}catch (e:Exception){
            null
        }

        return response
    }

    override suspend fun deleteReel(token: String, data: DeleteReelRequestDto): Boolean {
        val response = try{httpClient.delete(REELS_BASE_URL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun postReel(token: String, data: PostReelRequestDto): Boolean {
        val response = try{httpClient.post(POST_REEL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun uploadFile(url: String, data: String,contentType: ReelContentType): Boolean {
        val file = okio.FileSystem.SYSTEM.source(data.toPath())
        val response = try { httpClient.put(url) {
            header(HttpHeaders.ContentType,if (contentType == ReelContentType.IMAGE) ContentType.Image.PNG else ContentType.Video.MP4)
            setBody(file.buffer().readByteArray())
        }}catch (e:Exception){
            null
        }

        return response?.status?.value in 200..300
    }

    override suspend fun markReel(token: String, data: MarkReelRequestDto): Boolean {
        val response = try{httpClient.post(MARK_REEL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun reportReel(token: String, data: ReportReelDto): Boolean {
        val response = try{httpClient.post(REPORT_REEL) {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
            setBody(data)
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun markReelFavorite(token: String, reelId: String): Boolean {
        val response = try{httpClient.get("$MARK_FAVORITE/$reelId") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun markReelUnFavorite(token: String, reelId: String): Boolean {
        val response = try{ httpClient.delete("$MARK_FAVORITE/$reelId") {
            header("Content-Type","application/json")
            header("Authorization","Bearer $token")
        }}catch (e:Exception){
            null
        }
        return response?.status?.value in 200..300
    }

    companion object{
        private const val REELS_BASE_URL = "${AppConstants.BASE_REELS_URL}/reels"
        private const val REELS_FOR_YOU = "${REELS_BASE_URL}/for-you"
        private const val REELS_FROM_MATCHES = "${REELS_BASE_URL}/matches"
        private const val FILTER_REELS = "${REELS_BASE_URL}/filter"
        private const val FAVORITE_REELS = "${REELS_BASE_URL}/favorites"
        private const val REPORT_REEL = "${REELS_BASE_URL}/report"
        private const val MARK_FAVORITE = "${REELS_BASE_URL}/favorite"
        private const val MY_REELS = "${AppConstants.BASE_REELS_URL}/my-reels"
        private const val POST_REEL = "${AppConstants.BASE_REELS_URL}/post-reels"
        private const val MARK_REEL = "${AppConstants.BASE_REELS_URL}/mark-reels"
        private const val CREATE_PRESIGNED_URL = "${AppConstants.BASE_REELS_URL}/upload/signed-url"
    }
}