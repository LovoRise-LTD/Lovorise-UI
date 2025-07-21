package com.lovorise.app.reels.data.repo

import com.lovorise.app.reels.data.mapper.toCreateReelRequestDto
import com.lovorise.app.reels.data.mapper.toCreateUpdateReelResponse
import com.lovorise.app.reels.data.mapper.toFilterReelsRequestDto
import com.lovorise.app.reels.data.mapper.toMarkReelRequestDto
import com.lovorise.app.reels.data.mapper.toMyReelsResponse
import com.lovorise.app.reels.data.mapper.toReelSignedUrlRequestDto
import com.lovorise.app.reels.data.mapper.toReelSignedUrlResponse
import com.lovorise.app.reels.data.mapper.toReelsResponse
import com.lovorise.app.reels.data.mapper.toReportReelDto
import com.lovorise.app.reels.data.mapper.toUpdateReelRequestDto
import com.lovorise.app.reels.data.source.remote.ReelsApiService
import com.lovorise.app.reels.data.source.remote.dto.DeleteReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.PostReelRequestDto
import com.lovorise.app.reels.domain.models.CreateReelRequest
import com.lovorise.app.reels.domain.models.CreateUpdateReelResponse
import com.lovorise.app.reels.domain.models.FilterReelsRequest
import com.lovorise.app.reels.domain.models.MarkReelRequest
import com.lovorise.app.reels.domain.models.MyReelsResponse
import com.lovorise.app.reels.domain.models.ReelContentType
import com.lovorise.app.reels.domain.models.ReelSignedUrlRequest
import com.lovorise.app.reels.domain.models.ReelSignedUrlResponse
import com.lovorise.app.reels.domain.models.ReelsResponse
import com.lovorise.app.reels.domain.models.ReportReel
import com.lovorise.app.reels.domain.models.UpdateReelRequest
import com.lovorise.app.reels.domain.repo.ReelsRepo
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReelsRepoImpl(
    private val reelsApiService: ReelsApiService
) : ReelsRepo {

    override suspend fun getReelsForYou(token: String, page: Int): Flow<Resource<ReelsResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.getReelsForYou(token,page=page)
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toReelsResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }

    }

    override suspend fun getReelsFromMatches(token: String, page: Int): Flow<Resource<ReelsResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.getReelsFromMatches(token,page=page)
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toReelsResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun filterReels(token: String, data: FilterReelsRequest, page: Int): Flow<Resource<ReelsResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.filterReels(token,page=page, data = data.toFilterReelsRequestDto())
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toReelsResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun getFavoriteReels(token: String, page: Int): Flow<Resource<ReelsResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.getFavoriteReels(token,page=page)
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toReelsResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun getMyReels(token: String, page: Int): Flow<Resource<MyReelsResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.getMyReels(token,page=page)
            emit(Resource.Loading(false))
            if (response?.reels != null){
                emit(Resource.Success(response.toMyReelsResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun getPresignedUrl(token: String, data: ReelSignedUrlRequest): Flow<Resource<ReelSignedUrlResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.getPresignedUrl(token,data.toReelSignedUrlRequestDto())
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toReelSignedUrlResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun createReel(token: String, data: CreateReelRequest): Flow<Resource<CreateUpdateReelResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.createReel(token,data.toCreateReelRequestDto())
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toCreateUpdateReelResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun updateReel(token: String, data: UpdateReelRequest): Flow<Resource<CreateUpdateReelResponse?>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.updateReel(token,data.toUpdateReelRequestDto())
            emit(Resource.Loading(false))
            if (response?.error == false && response.data != null){
                emit(Resource.Success(response.toCreateUpdateReelResponse()))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun deleteReel(token: String, reelId: Int): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.deleteReel(token, DeleteReelRequestDto(reelId = reelId))
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun postReel(token: String, reelId: Int): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.postReel(token, PostReelRequestDto(reelId = reelId,isPosted = true))
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun uploadFile(url: String, data: String, contentType: ReelContentType): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.uploadFile(url, data, contentType)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun markReel(token: String, data: MarkReelRequest): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.markReel(token,data.toMarkReelRequestDto())
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun reportReel(token: String, data: ReportReel): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.reportReel(token,data.toReportReelDto())
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun markReelUnFavorite(token: String, reelId: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.markReelUnFavorite(token,reelId)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }

    override suspend fun markReelFavorite(token: String, reelId: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = reelsApiService.markReelFavorite(token,reelId)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Some error occurred"))
            }
        }
    }
}