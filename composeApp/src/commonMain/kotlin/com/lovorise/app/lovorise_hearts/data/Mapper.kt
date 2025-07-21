package com.lovorise.app.lovorise_hearts.data

import com.lovorise.app.lovorise_hearts.data.remote.dto.SpotlightResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.TransactionsResponse
import com.lovorise.app.lovorise_hearts.data.remote.dto.UserPurchaseRewardsResponse
import com.lovorise.app.lovorise_hearts.domain.model.PurchaseRewardsSpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.RewardType
import com.lovorise.app.lovorise_hearts.domain.model.RewardType.NONE
import com.lovorise.app.lovorise_hearts.domain.model.RewardType.entries
import com.lovorise.app.lovorise_hearts.domain.model.SpotlightData
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.profile.presentation.components.SpotlightType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.max

fun TransactionsResponse.TransactionDto.toTransactionData():TransactionData?{
    return if (id.isNullOrBlank() || type.isNullOrBlank() || name.isNullOrBlank() || timestamp.isNullOrBlank() || change == null) return null
    else TransactionData(
        id = id,
        change = change,
        type = if (type == "incoming") TransactionData.Type.INCOMING else TransactionData.Type.OUTGOING,
        name = name,
        timestamp = timestamp
    )
}

fun UserPurchaseRewardsResponse.toPurchaseRewardsData():PurchaseRewardsSpotlightData{
    return PurchaseRewardsSpotlightData(
        balance = balance?.balance ?: 0,
        spotlight = spotlight?.toSpotlightData(),
        subscription = subscription?.toSubscriptionData(),
        userRewards = userRewards?.mapNotNull { it?.toRewardData() } ?: emptyList()
    )
}

fun UserPurchaseRewardsResponse.RewardsResponse.toRewardData() : PurchaseRewardsSpotlightData.RewardData{
    return PurchaseRewardsSpotlightData.RewardData(
        isAvailable = isAvailable,
        type = try { fromValue(type) }catch (e:Exception){ NONE }
    )
}

fun fromValue(value: String): RewardType {
    return entries.find { it.value == value } ?: NONE
}

fun UserPurchaseRewardsResponse.SubscriptionStatus.toSubscriptionData():PurchaseRewardsSpotlightData.SubscriptionData{
    val expires = Instant.parse(endDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
    return PurchaseRewardsSpotlightData.SubscriptionData(
        type = if (plan == "mock_monthly") SubscriptionType.MONTHLY else if (plan.contains("week")) SubscriptionType.WEEKLY else SubscriptionType.FREE,
        startDate = startDate,
        endDate = endDate,
        isAvailable = isAvailable,
        formatDDMMYYYY = "${expires.dayOfMonth}.${expires.monthNumber}.${expires.year}",
        formatDDMONTHYEAR = "${expires.dayOfMonth} ${getMonthAbbreviation(expires.monthNumber)} ${expires.year}"
    )
}

fun getMonthAbbreviation(month: Int): String {
    val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    return months.getOrNull(month - 1) ?: "Invalid Month"
}

fun SpotlightResponse.toSpotlightData():SpotlightData{
    val givenTimestamp = Instant.parse(expiredAt)

    // Current time in UTC
    val currentTimestamp = Clock.System.now()

    // Calculate the difference in minutes
    val differenceInMinutes = (givenTimestamp - currentTimestamp).inWholeMinutes

    return SpotlightData(
        type = if (type == "Spotlight") SpotlightType.SPOTLIGHT else SpotlightType.SUPER_SPOTLIGHT,
        totalMinutes = if (type == "Spotlight") 60 else 1440,
        remainingMinutes = max(0,differenceInMinutes),
        expiresAt = expiredAt
    )
}