package com.lovorise.app.accounts.domain.model

import kotlinx.datetime.LocalDate

data class AppSettingsData(
    val language:String,
    val anonymousModeExpiry: LocalDate?,
    val controlProfile: ControlProfile,
    val notification: NotificationSettings,
    val readReceipt: Boolean,
    val isNotificationDataUpdated:Boolean = false,
    val travelTicketStatus:TravelTicketStatus?
){
    data class ControlProfile(
        val hideAge:Boolean,
        val onlineStatus:Boolean,
        val hideLocation:Boolean
    )

    data class NotificationSettings(
        val messages: Boolean,
        val likes: Boolean,
        val matches: Boolean,
        val visitors: Boolean,
        val secretCrush: Boolean,
        val reels: Boolean,
        val offerAndPromotions:Boolean
    )

    data class TravelTicketStatus(
        val enabled:Boolean,
        val destination: Destination,
        val departDate:String,
        val returnDate:String
    ){
        data class Destination(
            val country:String,
            val city:String,
            val longitude:Double,
            val latitude:Double
        )
    }
}
