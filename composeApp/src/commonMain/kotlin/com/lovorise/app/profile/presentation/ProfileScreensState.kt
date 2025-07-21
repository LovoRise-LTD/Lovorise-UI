package com.lovorise.app.profile.presentation

import com.lovorise.app.lovorise_hearts.domain.model.PurchaseRewardsSpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.SpotlightData

data class ProfileScreensState(
    val isLoading:Boolean = false,
    val subscriptionType: SubscriptionType = SubscriptionType.FREE,
    val subscriptionData:PurchaseRewardsSpotlightData.SubscriptionData?= null,
    val currentTabIndex:Int = 0,
    val showRewardsOverlay:Boolean = false,
    val spotlightData:SpotlightData? = null,
    val showPurchasedSpotlight:Boolean = false,
    val showContinueSubscriptionDialog:Boolean = false,
    val showSubscriptionDetailsDialog:Boolean = false,
    val showCancelConfirmationDialog:Boolean = false,
    val showSpotlightOptions: Boolean = false,
    val showGetVerifyBadgeDialog:Boolean = false,
    val showVerificationSuccessDialog:Boolean = false,
    val isVerificationUnderReview:Boolean = false,
    val showVerificationUnderReviewDialog:Boolean = false,
    val dailyLoginRewardCollected:Boolean = false,
    val isProfileSpotlight:Boolean = false,
    val hearts:Long = 0,
    val showHeartShopWelcomeDialog:Boolean? = null,
    val isOnboardingDataSent:Boolean = false,
    val rewards:List<PurchaseRewardsSpotlightData.RewardData> = emptyList(),
    val toastMessage:String = "",
    val shouldRefresh:Boolean = true
    //val success:Boolean = false
)
