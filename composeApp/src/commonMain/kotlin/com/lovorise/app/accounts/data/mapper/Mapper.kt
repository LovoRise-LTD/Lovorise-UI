package com.lovorise.app.accounts.data.mapper

import com.lovorise.app.accounts.data.sources.remote.dto.ControlMyViewDto
import com.lovorise.app.accounts.data.sources.remote.dto.GetMediasResponseDto
import com.lovorise.app.accounts.data.sources.remote.dto.NotificationSettingsDto
import com.lovorise.app.accounts.data.sources.remote.dto.UserResponseDto
import com.lovorise.app.accounts.domain.model.AccountSource
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.domain.model.GetMediaResponse
import com.lovorise.app.accounts.domain.model.SignedUrlMediaItem
import com.lovorise.app.accounts.domain.model.UserResponse
import com.lovorise.app.profile.presentation.edit_profile.DistanceMeasurement


fun GetMediasResponseDto.toImagesResponse():GetMediaResponse{
    return GetMediaResponse(
        medias = this.mediaData?.map { it?.toMediaData() }
    )
}

fun ControlMyViewDto.toControlProfile():AppSettingsData.ControlProfile{
    return AppSettingsData.ControlProfile(
        hideLocation = hideLocation ?: false,
        onlineStatus = onlineStatus ?: false,
        hideAge = hideMyAge ?: false
    )
}


fun NotificationSettingsDto.toNotificationData():AppSettingsData.NotificationSettings{
    return AppSettingsData.NotificationSettings(
        messages = messages ?: true,
        secretCrush = secretCrush ?: true,
        likes = likes ?: true,
        reels = reels ?: true,
        visitors = reels ?: true,
        matches = matches ?: true,
        offerAndPromotions = offerAndPromotion ?: false
    )
}

fun UserResponseDto.toUserResponse():UserResponse?{
    val userRes = user ?: return null
    return UserResponse(
        id = userRes.id,
        name = userRes.name,
        bio = userRes.bio,
        ethnicity = userRes.ethnicity,
        typeOfRelation = userRes.preferences?.typeOfRelation,
        education = userRes.education,
        pets = userRes.pets,
        religion = userRes.religion,
        language = userRes.language ?: emptyList(),
        drinking = userRes.preferences?.drinking,
        gender = userRes.gender,
        family = userRes.preferences?.family,
        smoking = userRes.preferences?.smoking,
        interests = userRes.interests,
        height = userRes.height,
        age = userRes.age,
        ageStart = userRes.preferences?.ageStart,
        ageEnd = userRes.preferences?.ageEnd,
        likeToMeet = userRes.preferences?.likeToMeet,
        distance = userRes.preferences?.distance,
        medias = medias?.mapNotNull { data -> data?.let { if (it.mediaId != null && it.mediaUrl != null && it.mediaName != null && it.orderNum != null && it.mediaType != null){  GetMediaResponse.MediaData(id = it.mediaId, url = it.mediaUrl, name = it.mediaName, orderNum = it.orderNum, thumbnail = it.thumbnailUrl,type = if (it.mediaType == "image") SignedUrlMediaItem.Type.IMAGE else SignedUrlMediaItem.Type.VIDEO)} else null }  },
        nameUpdatedAt = userRes.nameUpdatedAt,
        genderVisible = userRes.genderVisible,
        familyVisible = userRes.preferences?.familyVisible,
        bioVisible = userRes.bioVisible,
        ethnicityVisible = userRes.ethnicityVisible,
        educationVisible = userRes.educationVisible,
        smokingVisible = userRes.preferences?.smokingVisible,
        drinkingVisible = userRes.preferences?.drinkingVisible,
        petsVisible = userRes.petsVisible,
        languageVisible = userRes.languageVisible,
        religionVisible = userRes.religionVisible,
        likeToMeetVisible = userRes.preferences?.likeToMeetVisible,
        ageUpdatedAt = userRes.ageUpdatedAt,
        progress = progress,
        heightVisible = userRes.heightVisible,
        profession = UserResponse.ProfessionData(
            professionVisible = userRes.profession?.professionVisible,
            jobTitle = userRes.profession?.jobTitle,
            company = userRes.profession?.company
        ),
        locationData = UserResponse.LocationData(
            longitude = userRes.location?.longitude,
            latitude = userRes.location?.latitude,
            city = userRes.location?.city,
            country = userRes.location?.country
        ),
        birthDate = userRes.birthDate,
        status = userRes.status,
        email = userRes.email,
        isVerified = userRes.isVerified,
        source = try { AccountSource.valueOf(userRes.source?.uppercase() ?: "") } catch (e:IllegalArgumentException){ AccountSource.EMAIL },
        isNameUpdatable = userRes.nameUpdatable ?: false,
        isAgeUpdatable = userRes.ageUpdatable ?: false,
        isEmailVerified = userRes.isEmailVerified ?: false,
        isGenderUpdatable = true,
        extraGenderText = userRes.anotherGender,
        distanceMeasurement = if (userRes.preferences?.distanceMeasurement.isNullOrBlank() || userRes.preferences?.distanceMeasurement == "km") DistanceMeasurement.KM else DistanceMeasurement.MILES
    )
}

fun GetMediasResponseDto.MediaDataDto.toMediaData():GetMediaResponse.MediaData{
    return GetMediaResponse.MediaData(
        id = mediaId!!,
        url = mediaUrl!!,
        name = mediaName!!,
        orderNum = orderNum!!,
        type = if (mediaType == "image") SignedUrlMediaItem.Type.IMAGE else SignedUrlMediaItem.Type.VIDEO
    )
}