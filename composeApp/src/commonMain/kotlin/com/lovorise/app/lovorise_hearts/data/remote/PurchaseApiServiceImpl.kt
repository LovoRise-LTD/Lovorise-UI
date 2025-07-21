package com.lovorise.app.lovorise_hearts.data.remote

import com.lovorise.app.lovorise_hearts.data.remote.dto.DailyLoginRewardRequestData
import com.lovorise.app.lovorise_hearts.data.remote.dto.InviteFriendResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.OnboardingInfoRewardRequestData
import com.lovorise.app.lovorise_hearts.data.remote.dto.SpotlightResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.TransactionsResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.UserPurchaseRewardsResponse
import com.lovorise.app.util.AppConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class PurchaseApiServiceImpl(
    private val httpClient: HttpClient
) : PurchaseApiService {


    override suspend fun getSpotlight(token: String): SpotlightResponse? {
        val response: SpotlightResponse? = try {
            httpClient.post(SPOTLIGHT_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun getSuperSpotlight(token: String): SpotlightResponse? {
        val response: SpotlightResponse? = try {
            httpClient.post(SUPER_SPOTLIGHT_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun getPurchaseData(token: String): UserPurchaseRewardsResponse? {
        val response: UserPurchaseRewardsResponse? = try {
            httpClient.get(PURCHASE_INFO_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun claimOnboardingReward(token: String, info: String): Boolean {
        val response = try {
            httpClient.post(CLAIM_REWARD_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(OnboardingInfoRewardRequestData(challenge = OnboardingInfoRewardRequestData.Challenge(type = "onboarding",details = OnboardingInfoRewardRequestData.Challenge.Details(info))))
            }
        } catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun claimDailyLoginReward(token: String): Boolean {
        val response = try {
            httpClient.post(CLAIM_REWARD_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
                setBody(DailyLoginRewardRequestData(DailyLoginRewardRequestData.Challenge("daily_login")))
            }
        } catch (e: Exception) {
            null
        }
        return response?.status?.value in 200..300
    }

    override suspend fun getTransactions(token: String): TransactionsResponse? {
        val response: TransactionsResponse? = try {
            httpClient.get(GET_TRANSACTIONS_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    override suspend fun inviteFriend(token: String): InviteFriendResponse? {
        val response: InviteFriendResponse? = try {
            httpClient.get(INVITE_FRIEND_URL) {
                header("Content-Type", "application/json")
                header("Authorization", "Bearer $token")
            }.body()
        } catch (e: Exception) {
            null
        }
        return response
    }

    companion object{
        private const val GET_TRANSACTIONS_URL = "${AppConstants.BASE_PURCHASE_URL}/currency/transactions"
        private const val INVITE_FRIEND_URL = "${AppConstants.BASE_PURCHASE_URL}/rewards/invite-friends"
        private const val CLAIM_REWARD_URL = "${AppConstants.BASE_PURCHASE_URL}/currency/earn"
        private const val SPOTLIGHT_URL = "${AppConstants.BASE_PURCHASE_URL}/powerups/spotlight"
        private const val SUPER_SPOTLIGHT_URL = "${AppConstants.BASE_PURCHASE_URL}/powerups/super-spotlight"
        private const val PURCHASE_INFO_URL = "${AppConstants.BASE_PURCHASE_URL}/user/info"
    }

}