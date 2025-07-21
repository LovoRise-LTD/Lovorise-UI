package com.lovorise.app.reels.presentation.states

import com.lovorise.app.reels.domain.models.CreateReelRequest
import com.lovorise.app.reels.domain.models.MyReelsResponse
import com.lovorise.app.reels.domain.models.ReelSignedUrlRequest
import com.lovorise.app.reels.domain.models.ReelsResponse

data class ReelsApiState(
    val isLoading:Boolean = false,
    val myReels:MyReelsResponse = MyReelsResponse(reels = emptyList()),
    val reelsForYou:ReelsResponse? = null,
    val reelsFromMatches:ReelsResponse? = null,
    val favoriteReels:ReelsResponse? = null,
    val favoriteReelIds:List<Int> = emptyList(),
    val watchedReelsId:List<Int> = emptyList(),
    val toastMessage:String = "",
    val processingReel:ProcessingReel?=null,
    val isUploading:Boolean = false
){
    data class ProcessingReel(
        val file:String,
        val uploadData: ReelSignedUrlRequest,
        val createData: CreateReelRequest
    )
}
