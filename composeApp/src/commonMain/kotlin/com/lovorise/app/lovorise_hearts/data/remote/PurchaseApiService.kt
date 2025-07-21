package com.lovorise.app.lovorise_hearts.data.remote

import com.lovorise.app.lovorise_hearts.data.remote.dto.InviteFriendResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.SpotlightResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.TransactionsResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.UserPurchaseRewardsResponse

interface PurchaseApiService {

    suspend fun getTransactions(token:String):TransactionsResponse?
    suspend fun inviteFriend(token:String):InviteFriendResponse?

    suspend fun getSpotlight(token: String) : SpotlightResponse?
    suspend fun getSuperSpotlight(token: String) : SpotlightResponse?
    suspend fun getPurchaseData(token: String) : UserPurchaseRewardsResponse?
    suspend fun claimOnboardingReward(token: String,info:String) : Boolean
    suspend fun claimDailyLoginReward(token: String) : Boolean

}