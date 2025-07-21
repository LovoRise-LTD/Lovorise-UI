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

interface ReelsApiService {

    suspend fun getReelsForYou(token:String,limit:Int = 20,page:Int) : ReelsResponseDto?
    suspend fun getReelsFromMatches(token:String,limit:Int = 20,page:Int) : ReelsResponseDto?
    suspend fun filterReels(token:String,limit:Int = 20,data:FilterReelsRequestDto,page:Int) : ReelsResponseDto?
    suspend fun getFavoriteReels(token: String,limit: Int = 20,page:Int) : FavouritesReelsResponseDto?

    suspend fun getMyReels(token: String,limit:Int = 20,page:Int) : MyReelsResponseDto?
    suspend fun getPresignedUrl(token: String,data:ReelSignedUrlRequestDto) : ReelSignedUrlResponseDto?
    suspend fun createReel(token: String, data:CreateReelRequestDto) : CreateUpdateReelResponseDto?
    suspend fun updateReel(token: String, data:UpdateReelRequestDto) : CreateUpdateReelResponseDto?
    suspend fun deleteReel(token: String,data:DeleteReelRequestDto) : Boolean
    suspend fun postReel(token: String,data:PostReelRequestDto) : Boolean

    suspend fun uploadFile(url:String,data:String,contentType:ReelContentType) : Boolean

    suspend fun markReel(token: String,data:MarkReelRequestDto) : Boolean
    suspend fun reportReel(token: String,data:ReportReelDto) : Boolean
    suspend fun markReelFavorite(token: String,reelId:String) : Boolean
    suspend fun markReelUnFavorite(token: String,reelId:String) : Boolean


}

