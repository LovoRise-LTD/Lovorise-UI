package com.lovorise.app.accounts.domain.model

data class GetMediaResponse(
    val medias:List<MediaData?>?
){
    data class MediaData(
        val id:String,
        val url:String,
        val name:String,
        val orderNum:Int,
        val localPath:String?=null,
        val thumbnail:String?=null,
        val type: SignedUrlMediaItem.Type
    )
}
