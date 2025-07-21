package com.lovorise.app.lovorise_hearts.data.repo

import com.lovorise.app.invite.InviteFriendData
import com.lovorise.app.lovorise_hearts.data.remote.PurchaseApiService
import com.lovorise.app.lovorise_hearts.data.toPurchaseRewardsData
import com.lovorise.app.lovorise_hearts.data.toSpotlightData
import com.lovorise.app.lovorise_hearts.data.toTransactionData
import com.lovorise.app.lovorise_hearts.domain.PurchaseRepo
import com.lovorise.app.lovorise_hearts.domain.model.PurchaseRewardsSpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.SpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.profile.presentation.components.SpotlightType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PurchaseRepoImpl(
    private val purchaseApiService: PurchaseApiService
) : PurchaseRepo {

    override suspend fun getSpotlight(token: String, type: SpotlightType): Flow<Resource<SpotlightData>> {
        return flow {
            emit(Resource.Loading(true))
            val response = if (type == SpotlightType.SPOTLIGHT)  purchaseApiService.getSpotlight(token) else purchaseApiService.getSuperSpotlight(token)
            emit(Resource.Loading(false))
            emit(Resource.Success(response?.toSpotlightData()))
        }
    }

    override suspend fun getPurchaseData(token: String): Flow<Resource<PurchaseRewardsSpotlightData>> {
        return flow {
            emit(Resource.Loading(true))
            val response = purchaseApiService.getPurchaseData(token = token)
            emit(Resource.Loading(false))
            emit(Resource.Success(response?.toPurchaseRewardsData()))
        }
    }

    override suspend fun claimOnboardingReward(token: String, info: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = purchaseApiService.claimOnboardingReward(token = token, info = info)
            emit(Resource.Loading(false))
            if (response){
                emit(Resource.Success(response))
            }else{
                emit(Resource.Error("Rewards failed"))
            }
        }
    }

    override suspend fun claimDailyLoginReward(token: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading(true))
            val response = purchaseApiService.claimDailyLoginReward(token = token)
            emit(Resource.Loading(false))
            emit(Resource.Success(response))
        }
    }

    override suspend fun getTransactions(token: String): Flow<Resource<List<TransactionData>>> {
        return flow {
            emit(Resource.Loading(true))
            val response = purchaseApiService.getTransactions(token)
            emit(Resource.Loading(false))
            val transactions = response?.transactions?.mapNotNull { it?.toTransactionData() } ?: emptyList()
            emit(Resource.Success(transactions))
        }
    }

    override suspend fun inviteFriends(token: String): Flow<Resource<InviteFriendData>> {
        return flow {
            emit(Resource.Loading(true))
            val response = purchaseApiService.inviteFriend(token)
            emit(Resource.Loading(false))
            if (response?.inviteUrl.isNullOrBlank()) {
                emit(Resource.Error("Error Inviting Friends"))
            }else{
                emit(Resource.Success(InviteFriendData(inviteUrl = response?.inviteUrl ?: "", users = emptyList())))
            }
        }
    }
}