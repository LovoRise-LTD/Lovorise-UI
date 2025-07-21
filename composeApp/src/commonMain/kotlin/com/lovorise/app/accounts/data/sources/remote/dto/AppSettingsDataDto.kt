package com.lovorise.app.accounts.data.sources.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsDataDto(
    val anonymousStatus: AnonymousStatusResponseDto?,
    val controlMyView: ControlMyViewDto?,
    val userAppLanguage: GetLanguagesResponseDto?,
    val notifications: NotificationSettingsDto?,
    val readReceiptStatus:ReadReceiptDto?,
    val travelTicketStatus:TravelTicketStatusDto?
){
    @Serializable
    data class ReadReceiptDto(
        val enabled:Boolean?
    )

    @Serializable
    data class TravelTicketStatusDto(
        val enabled:Boolean?,
        val destination: DestinationDto?,
        val departDate:String? = null,
        val returnDate:String? = null
    ){
        @Serializable
        data class DestinationDto(
            val country:String?,
            val city:String?,
            val longitude:Double?,
            val latitude:Double?
        )
    }
}
