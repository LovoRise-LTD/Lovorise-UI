package com.lovorise.app.lovorise_hearts.domain

import com.lovorise.app.invite.InviteFriendData
import com.lovorise.app.lovorise_hearts.domain.model.PurchaseRewardsSpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.SpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.profile.presentation.components.SpotlightType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface PurchaseRepo {
    suspend fun getTransactions(token:String):Flow<Resource<List<TransactionData>>>
    suspend fun inviteFriends(token:String) : Flow<Resource<InviteFriendData>>
    suspend fun getSpotlight(token: String,type:SpotlightType) : Flow<Resource<SpotlightData>>
    suspend fun getPurchaseData(token: String) : Flow<Resource<PurchaseRewardsSpotlightData>>
    suspend fun claimOnboardingReward(token: String,info:String) : Flow<Resource<Boolean>>
    suspend fun claimDailyLoginReward(token: String) : Flow<Resource<Boolean>>
}