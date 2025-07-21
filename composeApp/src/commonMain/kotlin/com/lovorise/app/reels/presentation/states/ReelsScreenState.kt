package com.lovorise.app.reels.presentation.states

data class ReelsScreenState(
    val swipeUpAnimation:String = "",
    val showReportReelBottomSheet:Boolean = false,
    val toastMessage:String = "",
    val showNotInterestedOptions:Boolean = false,
    val showReportOptions:Boolean = false,
    val showShareReelBottomSheet:Boolean = false,
    val showSwipeUpAnimation:Boolean = false,
    val isVideoPlaying:Boolean = true,
    val isVideoMuted:Boolean = false,
    val showPauseButton:Boolean = false,
    val currentPosition:Long = 0,
    val totalDuration:Long = 0,
    val currentSliderPos:Float = 0f
)
