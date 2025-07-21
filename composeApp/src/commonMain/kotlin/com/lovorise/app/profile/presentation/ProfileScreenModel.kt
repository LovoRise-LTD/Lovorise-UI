package com.lovorise.app.profile.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.lovorise_hearts.data.getMonthAbbreviation
import com.lovorise.app.lovorise_hearts.domain.PurchaseRepo
import com.lovorise.app.lovorise_hearts.domain.model.PurchaseRewardsSpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.RewardType
import com.lovorise.app.profile.presentation.components.SpotlightType
import com.lovorise.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.max

class ProfileScreenModel (private val purchaseRepo: PurchaseRepo): ScreenModel {

    private val _state = MutableStateFlow(ProfileScreensState())
    val state = _state.asStateFlow()
    private var prefs:SharedPrefs? = null

    fun updateSubscriptionType(type: SubscriptionType){
        _state.update {
            it.copy(
                subscriptionType = type
            )
        }
    }

    fun updateToast(value:String){
        _state.update{
            it.copy(
                toastMessage = value
            )
        }
    }

    fun updateSubscriptionData(type:SubscriptionType){
        val expires = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _state.update {
            it.copy(
                subscriptionType = type,
                subscriptionData = PurchaseRewardsSpotlightData.SubscriptionData(
                    type = type,
                    startDate = "",
                    endDate = "",
                    isAvailable = true,
                    formatDDMMYYYY = "${expires.dayOfMonth}.${expires.monthNumber}.${expires.year}",
                    formatDDMONTHYEAR = "${expires.dayOfMonth} ${getMonthAbbreviation(expires.monthNumber)} ${expires.year}"
                )
            )
        }
    }

    fun updateRefresh(value: Boolean){
        _state.update {
            it.copy(
                shouldRefresh = value
            )
        }
    }

    fun getPurchaseInfo(context: PlatformContext){
        if (!state.value.shouldRefresh) return
        val token = getToken(context) ?: return
        screenModelScope.launch {
            purchaseRepo.getPurchaseData(token).collectLatest { res->
                if (res.data != null){
                    _state.update {
                        it.copy(
                            shouldRefresh = false,
                            hearts = res.data.balance.toLong(),
                            subscriptionData = res.data.subscription,
                            subscriptionType = res.data.subscription?.type ?: SubscriptionType.FREE,
                            rewards = res.data.userRewards,
                            spotlightData = res.data.spotlight,
                            isProfileSpotlight = (res.data.spotlight?.remainingMinutes ?: 0) > 0,
                            dailyLoginRewardCollected = !(res.data.userRewards.firstOrNull{r-> r.type == RewardType.DAILY_LOGIN}?.isAvailable ?: false),
                            isOnboardingDataSent = !(res.data.userRewards.firstOrNull{r-> r.type == RewardType.ONBOARDING}?.isAvailable ?: false)
                        )
                    }
                }
            }
        }
    }

    private fun getToken(context: PlatformContext):String?{
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        return prefs?.getString(PreferencesKeys.AUTH_TOKEN,null)
    }

    fun updateHeartShopWelcomeDialogState(context: PlatformContext){
        if (prefs == null) {
            prefs = SharedPrefsImpl(context)
        }
        val isShown = prefs?.getBoolean(PreferencesKeys.IS_HEART_SHOP_WELCOME_DIALOG_SHOWN,false) ?: false
        _state.update{
            it.copy(
                showHeartShopWelcomeDialog = !isShown
            )
        }
    }

    fun setHeartShopWelcomeDialogPrefs(context: PlatformContext){
        screenModelScope.launch {
            _state.update{
                it.copy(
                    showHeartShopWelcomeDialog = false
                )
            }
            val prefs = SharedPrefsImpl(context)
            prefs.setBoolean(PreferencesKeys.IS_HEART_SHOP_WELCOME_DIALOG_SHOWN,true)
        }

    }


    fun updateLoading(value: Boolean){
        _state.update{
            it.copy(
                isLoading = value
            )
        }
    }

    fun verifyPurchase(verifier: suspend ()->Boolean,hearts:Long){
        screenModelScope.launch {
            _state.update{
                it.copy(
                    isLoading = true
                )
            }
            val result = verifier()

            val updatedHearts = if (result) state.value.hearts + hearts else state.value.hearts
            _state.update{
                it.copy(
                    isLoading = false,
                    hearts = updatedHearts
                )
            }
        }
    }

    fun collectDailyLoginReward(context: PlatformContext){
        if (state.value.dailyLoginRewardCollected) return
        val token = getToken(context) ?: return
        screenModelScope.launch {
            purchaseRepo.claimDailyLoginReward(token).collectLatest { res->
                when(res){
                    is Resource.Success -> {
                        if (res.data == true){
                            _state.update {
                                it.copy(
                                    dailyLoginRewardCollected = true,
                                    hearts = state.value.hearts + 100,
                                    showRewardsOverlay = true
                                )
                            }
                        }
                    }
                    is Resource.Loading ->{
//                        _state.update {
//                            it.copy(
//                                isLoading = res.isLoading
//                            )
//                        }
                    }
                    is Resource.Error -> {}
                }

            }
        }
    }

    fun collectOnboardingReward(context: PlatformContext,data:String,onError:()->Unit){
        if (state.value.isOnboardingDataSent) return
        val token = getToken(context) ?: return
        screenModelScope.launch {
            purchaseRepo.claimOnboardingReward(token = token, info = data).collectLatest { res->
                when(res){
                    is Resource.Success ->{
                        _state.update {
                            it.copy(
                                isOnboardingDataSent = true,
                                hearts = state.value.hearts + 50,
                                showRewardsOverlay = true
                            )
                        }
                    }
                    is Resource.Loading ->{
                        _state.update {
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }
                    is Resource.Error -> {
                        onError()
                    }
                }

            }
        }
    }


    fun updateVerificationSuccessDialog(value: Boolean){
        _state.update{
            it.copy(
                showVerificationSuccessDialog = value
            )
        }
    }

    fun updateGetVerifyBadgeDialogState(value: Boolean){
        _state.update {
            it.copy(
                showGetVerifyBadgeDialog = value
            )
        }
    }


    fun updateVerificationUnderReviewDialogState(value: Boolean){
        _state.update {
            it.copy(
                showVerificationUnderReviewDialog = value
            )
        }
        if (value){
            _state.update{
                it.copy(
                    isVerificationUnderReview = true
                )
            }
        }
    }

    fun updatePurchasedSpotlight(value:SpotlightType,context: PlatformContext,onNavigateToHearts:()->Unit){
        if (getRemainingTime() > 0) {
            _state.update {
                it.copy(
                    toastMessage = "Already in spotlight"
                )
            }
            return
        }
        if ((value == SpotlightType.SPOTLIGHT && state.value.hearts < 100) || (value == SpotlightType.SUPER_SPOTLIGHT && state.value.hearts < 1000)){
            _state.update {
                it.copy(
                    toastMessage = "Insufficient balance"
                )
            }
            onNavigateToHearts()
            return
        }
        val token = getToken(context) ?: return

        screenModelScope.launch {
            purchaseRepo.getSpotlight(token = token, type = value).collectLatest { res ->
                when(res){
                    is Resource.Success ->{
                        if (res.data != null){
                            _state.update {
                                it.copy(
                                    hearts = if(value == SpotlightType.SPOTLIGHT) state.value.hearts - 100 else state.value.hearts - 1000,
                                    spotlightData = res.data,
                                    showPurchasedSpotlight = true,
                                    isProfileSpotlight = true
                                )
                            }
                        }
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = res.isLoading
                            )
                        }
                    }
                }
            }
        }
    }


    fun updateRemainingTime(){
        if (state.value.spotlightData == null) return
        _state.update {
            it.copy(
                spotlightData = state.value.spotlightData?.copy(
                    remainingMinutes = getRemainingTime()
                )
            )
        }
    }

    fun getRemainingTimeFormatted(): String? {
        val spotlightData = state.value.spotlightData ?: return null
        val expires = Instant.parse(spotlightData.expiresAt).toEpochMilliseconds()
        val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
        val remainingMillis = (expires - currentTimeMillis).coerceAtLeast(0L)

        if (remainingMillis == 0L) return  null

        val hours = remainingMillis / (1000 * 60 * 60)
        val minutes = (remainingMillis / (1000 * 60)) % 60
        val seconds = (remainingMillis / 1000) % 60

        return buildString {
            if (spotlightData.type == SpotlightType.SUPER_SPOTLIGHT) {
                append(if (hours < 10) "0$hours" else "$hours")
                append(":")
            }
            append(if (minutes < 10) "0$minutes" else "$minutes")
            append(":")
            append(if (seconds < 10) "0$seconds" else "$seconds")
        }
    }

    private fun getRemainingTime():Long{
        val spotlightData = state.value.spotlightData ?: return 0
        val givenTimestamp = Instant.parse(spotlightData.expiresAt)
        // Current time in UTC
        val currentTimestamp = Clock.System.now()
        // Calculate the difference in minutes
        val differenceInMinutes = (givenTimestamp - currentTimestamp).inWholeMinutes
        return max(0,differenceInMinutes)
    }

    fun updateShowPurchasedSpotlightState(value: Boolean){
        _state.update {
            it.copy(
                showPurchasedSpotlight = value
            )
        }
    }

    fun updateShowSpotlightOptionsState(value: Boolean){
        _state.update {
            it.copy(
                showSpotlightOptions = value
            )
        }
    }

    fun updateShowSubscriptionDetailsDialogState(value: Boolean){
        _state.update {
            it.copy(
                showSubscriptionDetailsDialog = value
            )
        }
    }

    fun updateShowContinueSubscriptionDialogState(value: Boolean){
        _state.update {
            it.copy(
                showContinueSubscriptionDialog = value
            )
        }
    }

    fun updateShowCancelConfirmationDialogState(value: Boolean){
        _state.update {
            it.copy(
                showCancelConfirmationDialog = value
            )
        }
    }

    fun updateCurrentTabIndex(index: Int){
        _state.update {
            it.copy(
                currentTabIndex = index
            )
        }
    }

    fun updateShowRewardsOverlayState(value:Boolean){
        _state.update {
            it.copy(
                showRewardsOverlay = value
            )
        }
    }



}