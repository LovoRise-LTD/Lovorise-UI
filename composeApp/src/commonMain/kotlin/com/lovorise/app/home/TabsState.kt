package com.lovorise.app.home

data class TabsState(
    val navStack:List<TabsScreenModel.BottomTab> = listOf(TabsScreenModel.BottomTab.SWIPE),
    val activeTab:TabsScreenModel.BottomTab = TabsScreenModel.BottomTab.SWIPE,
    val isGuideLinesShown:Boolean = false,
    val shouldRefresh:Boolean = true
)

// https://files.lovorise.org/reels/1729824284658743783_reel_1.mp4
// https://files.lovorise.org/reels/1729824587637322994_reel_2.mp4
// https://files.lovorise.org/reels/1729824695706307050_reel_3.mp4