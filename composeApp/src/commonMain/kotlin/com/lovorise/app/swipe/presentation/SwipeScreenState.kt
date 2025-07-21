package com.lovorise.app.swipe.presentation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lovorise.app.swipe.domain.SwipeProfileUser
import com.lovorise.app.swipe.presentation.components.SwipeDialogType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class SwipeScreenState(
    val isLoading:Boolean = false,
    val profiles:List<SwipeProfileUser> = emptyList(),
    val presentedDialogs:List<SwipeDialogType> = emptyList(),
    val showSwipeGuide: Boolean = false,
    val showToast :Boolean = false,
    val showMenuItem: Boolean = false,
    val showBlockDialog: Boolean = false,
    val showReportBottomSheet: Boolean = false,
    val showTopBar:Boolean = false,
    val spacing:Dp = 74.dp,
    val isSwipeGuideShown:Boolean = false,
    val isPrefsDataLoaded:Boolean = false,
    val skipTemporarily:Boolean = false,
    val likeProfile:Boolean = false,
    val dislikeProfile:Boolean = false,
    val currentlyVisibleProfileId:String = "",
    val shouldReloadProfiles:Boolean = true
)

@OptIn(ExperimentalUuidApi::class)
data class Profile(
    val id:String = Uuid.random().toString(),
    val offsetY:Float = 0f,
    val url:String
)
