package com.lovorise.app.reels.data.mapper

import com.lovorise.app.reels.data.source.remote.dto.CreateReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.CreateUpdateReelResponseDto
import com.lovorise.app.reels.data.source.remote.dto.FavouritesReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.FilterReelsRequestDto
import com.lovorise.app.reels.data.source.remote.dto.MarkReelRequestDto
import com.lovorise.app.reels.data.source.remote.dto.MyReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.ReelSignedUrlRequestDto
import com.lovorise.app.reels.data.source.remote.dto.ReelSignedUrlResponseDto
import com.lovorise.app.reels.data.source.remote.dto.ReelsResponseDto
import com.lovorise.app.reels.data.source.remote.dto.ReportReelDto
import com.lovorise.app.reels.data.source.remote.dto.UpdateReelRequestDto
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


fun MarkReelRequest.toMarkReelRequestDto(): MarkReelRequestDto{
    return MarkReelRequestDto(
        reelId = reelId,
        markType = markType.code,
    )
}


fun UpdateReelRequest.toUpdateReelRequestDto(): UpdateReelRequestDto{
    return UpdateReelRequestDto(
        privacySetting = privacySetting,
        caption = caption,
        reelId = reelId
    )
}


fun CreateUpdateReelResponseDto.toCreateUpdateReelResponse(): CreateUpdateReelResponse {
    return CreateUpdateReelResponse(
        updatedAt = data?.updatedAt,
        createdAt = data?.createdAt,
        contentId = data?.contentId,
        privacySetting = data?.privacySetting,
        statusId = data?.statusId,
        caption = data?.caption,
        id = data?.id,
        authId = data?.authId
    )
}


fun CreateReelRequest.toCreateReelRequestDto():CreateReelRequestDto{
    return CreateReelRequestDto(
        privacySetting = privacySetting,
        caption = caption,
        contentId = contentId
    )
}


fun ReelSignedUrlResponseDto.toReelSignedUrlResponse(): ReelSignedUrlResponse {
    return ReelSignedUrlResponse(
        presignUrl = data?.presignUrl,
        contentId = data?.contentId,
    )
}

fun ReelSignedUrlRequest.toReelSignedUrlRequestDto(): ReelSignedUrlRequestDto{
    return ReelSignedUrlRequestDto(
        mediaType = if (mediaType == ReelContentType.IMAGE) "image" else "video",
        fileName = fileName,
        originalAudio = originalAudio
    )
}


fun MyReelsResponseDto.toMyReelsResponse():MyReelsResponse{
    return MyReelsResponse(
        reels = reels?.map { it?.toMyReelItem() }
    )
}

fun MyReelsResponseDto.MyReelItemDto.toMyReelItem():MyReelsResponse.MyReelItem{
    return MyReelsResponse.MyReelItem(
        id = id,
        createdAt = createdAt,
        reelsContentProcessed = reelsContentProcessed?.map { it?.toMyReelProcessedItem() },
        statusId = statusId,
        caption = caption,
        reelsContent = reelsContent?.toMyReelContent(),
        privacySetting = privacySetting,
        updatedAt = updatedAt,
        watchCounter = watchCounter,
        favoriteCounter = favoriteCounter,
        shareCounter = shareCounter
    )
}

fun MyReelsResponseDto.MyReelItemDto.ReelsContentDto.toMyReelContent():MyReelsResponse.MyReelItem.ReelsContent{
    return MyReelsResponse.MyReelItem.ReelsContent(
        mediaType = if (mediaType == "image") ReelContentType.IMAGE else ReelContentType.VIDEO,
        mediaUrl = mediaUrl
    )
}

fun MyReelsResponseDto.MyReelItemDto.ReelsContentProcessedItemDto.toMyReelProcessedItem():MyReelsResponse.MyReelItem.ReelsContentProcessedItem{
    return MyReelsResponse.MyReelItem.ReelsContentProcessedItem(
        quality = quality,
        mediaUrl = mediaUrl,
        thumbnailUrl = thumbnailUrl
    )

}


fun FilterReelsRequest.toFilterReelsRequestDto():FilterReelsRequestDto{
    return FilterReelsRequestDto(
        premiumFilter = premiumFilter?.toPremiumFilterDto(),
        distancePreference = distancePreference,
        ageStart = ageStart,
        ageEnd = ageEnd,
        showMe = showMe,
        sortBy = sortBy
    )
}

fun FilterReelsRequest.PremiumFilter.toPremiumFilterDto():FilterReelsRequestDto.PremiumFilterDto{
    return FilterReelsRequestDto.PremiumFilterDto(
        heightStart = heightStart,
        heightEnd = heightEnd,
        datingPreference = datingPreference,
        showVerified = showVerified,
        religion = religion,
        familyPlans = familyPlans,
        educationLevel = educationLevel,
        language = language
    )
}


fun ReelsResponseDto.toReelsResponse() : ReelsResponse{
    return ReelsResponse(
        reels = data?.reels?.map { it?.toReelItem() }
    )

}

fun FavouritesReelsResponseDto.toReelsResponse():ReelsResponse{
    return ReelsResponse(
        reels = data?.reels?.map { it?.toReelItem() }
    )
}

fun ReportReel.toReportReelDto():ReportReelDto{
    return ReportReelDto(
        reelId = reelId,
        reasons = reasons
    )
}

fun FavouritesReelsResponseDto.ReelsDataDto.ReelDataItemDto.toReelItem():ReelsResponse.ReelDataItem{
    return ReelsResponse.ReelDataItem(
        reelId = reelId,
        createdAt = createdAt,
        mediaType = if (mediaType == "image") ReelContentType.IMAGE else ReelContentType.VIDEO,
        statusId = statusId,
        userData = userData?.toUserData(),
        caption = caption,
        content = content.firstOrNull()?.toReelContent(),
        originalAudio = originalAudio,
        isFavorite = isFavorite ?: false
    )
}

fun ReelsResponseDto.ReelsDataDto.ReelDataItemDto.toReelItem():ReelsResponse.ReelDataItem{
    return ReelsResponse.ReelDataItem(
        reelId = reelId,
        createdAt = createdAt,
        mediaType = if (mediaType == "image") ReelContentType.IMAGE else ReelContentType.VIDEO,
        statusId = statusId,
        userData = userData?.toUserData(),
        caption = caption,
        content = content?.toReelContent(),
        originalAudio = originalAudio,
        isFavorite = isFavorite ?: false
    )
}

fun ReelsResponseDto.ReelsDataDto.ReelDataItemDto.ReelContentDto.toReelContent():ReelsResponse.ReelDataItem.ReelContent{
    return ReelsResponse.ReelDataItem.ReelContent(
        thumbnailUrl = thumbnailUrl,
        mediaUrl = mediaUrl,
        quality = quality
    )

}

fun ReelsResponseDto.ReelsDataDto.ReelDataItemDto.UserDataDto.toUserData():ReelsResponse.ReelDataItem.UserData{
    return ReelsResponse.ReelDataItem.UserData(
        city = city,
        name = name,
        age = age,
        distance = distance,
        country = country,
        verified = verified,
        imageUrl = imageUrl
    )
}

