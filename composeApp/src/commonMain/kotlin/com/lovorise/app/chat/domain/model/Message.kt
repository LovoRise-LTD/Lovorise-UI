package com.lovorise.app.chat.domain.model

import com.lovorise.app.MediaItem
import com.lovorise.app.chat.link_og_tag.LinkPreviewData
import com.lovorise.app.chat.presentation.GiftData
import kotlinx.datetime.Clock


data class Message(
    val id:String= Clock.System.now().toEpochMilliseconds().toString(),
    val type: Type = Type.TEXT,
    val text:String = "",
    val formattedTimestamp:String = "",
    val conversationId:String,
    val timestamp:String,
    val senderUserId:String,
    val replyData: ReplyData? = null,
    val linkPreviewData:LinkPreviewData?=null,
    val previewProcessed:Boolean = false,
    val giftData:GiftData? = null,
    val separatorText: String = "",
    val medias:List<MediaItem> = emptyList()
){
    data class ReplyData(
        val title:String,
        val body:String
    )

    enum class Type{
        GIFT,TEXT,MEDIA
    }
}
