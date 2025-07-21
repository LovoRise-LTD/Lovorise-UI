package com.lovorise.app.lovorise_hearts.domain.model

import com.lovorise.app.profile.presentation.SubscriptionType

data class PurchaseRewardsSpotlightData(
    val balance:Int,
    val subscription:SubscriptionData?,
    val userRewards:List<RewardData>,
    val spotlight: SpotlightData?
) {

    data class SubscriptionData(
        val type: SubscriptionType,
        val startDate: String,
        val endDate: String,
        val isAvailable: Boolean,
        val formatDDMONTHYEAR:String,
        val formatDDMMYYYY:String
    )

    data class RewardData(
        val type: RewardType,
        val isAvailable: Boolean
    )
}

enum class RewardType(val value:String){
    SHARE_REELS("share_reels"),CREATE_REELS("create_reels"),RATE_APP("rate_app"),
    ADD_INTERESTS("add_interests"),VERIFY_PHOTOS("verify_photos"),UPLOAD_MEDIA("upload_user_media"),
    BIO("bio_not_empty"),INVITE_FRIEND("invite_friend"),DAILY_LOGIN("daily_login"),
    ONBOARDING("onboarding"),
    NONE("");
}
