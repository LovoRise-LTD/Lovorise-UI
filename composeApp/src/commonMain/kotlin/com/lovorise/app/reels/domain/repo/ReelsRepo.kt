package com.lovorise.app.reels.domain.repo


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
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface ReelsRepo {

    suspend fun getReelsForYou(token:String,page:Int) : Flow<Resource<ReelsResponse?>>
    suspend fun getReelsFromMatches(token:String,page:Int) : Flow<Resource<ReelsResponse?>>
    suspend fun filterReels(token:String, data: FilterReelsRequest, page:Int) : Flow<Resource<ReelsResponse?>>
    suspend fun getFavoriteReels(token: String,page:Int) : Flow<Resource<ReelsResponse?>>

    suspend fun getMyReels(token: String,page:Int) : Flow<Resource<MyReelsResponse?>>
    suspend fun getPresignedUrl(token: String,data: ReelSignedUrlRequest) : Flow<Resource<ReelSignedUrlResponse?>>
    suspend fun createReel(token: String, data: CreateReelRequest) : Flow<Resource<CreateUpdateReelResponse?>>
    suspend fun updateReel(token: String, data: UpdateReelRequest) : Flow<Resource<CreateUpdateReelResponse?>>
    suspend fun deleteReel(token: String,reelId:Int) : Flow<Resource<Boolean>>
    suspend fun postReel(token: String,reelId:Int) : Flow<Resource<Boolean>>

    suspend fun uploadFile(url:String,data:String,contentType:ReelContentType) : Flow<Resource<Boolean>>

    suspend fun markReel(token: String,data: MarkReelRequest) : Flow<Resource<Boolean>>
    suspend fun reportReel(token: String,data: ReportReel) : Flow<Resource<Boolean>>
    suspend fun markReelFavorite(token: String,reelId:String) : Flow<Resource<Boolean>>
    suspend fun markReelUnFavorite(token: String,reelId:String) : Flow<Resource<Boolean>>
}