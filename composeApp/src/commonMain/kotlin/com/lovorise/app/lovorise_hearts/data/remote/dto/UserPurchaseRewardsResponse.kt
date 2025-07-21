package com.lovorise.app.lovorise_hearts.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserPurchaseRewardsResponse(
    val balance:BalanceResponse?,
    val subscription:SubscriptionStatus?,
    val userRewards:List<RewardsResponse?>?,
    val spotlight:SpotlightResponse?
){

    @Serializable
    data class BalanceResponse(
        val balance:Int
    )

    @Serializable
    data class SubscriptionStatus(
        val plan:String,
        val startDate:String,
        val endDate:String,
        val isAvailable: Boolean
    )

    @Serializable
    data class RewardsResponse(
        val type:String,
        @SerialName("is_available")
        val isAvailable:Boolean
    )






}
