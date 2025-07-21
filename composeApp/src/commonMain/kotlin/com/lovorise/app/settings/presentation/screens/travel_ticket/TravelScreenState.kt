package com.lovorise.app.settings.presentation.screens.travel_ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TravelScreenState(
    val selectedDestination:Airport? = null,
    val depatureDate:String="",
    val depatureDateTimeStamp:Long? =null,
    val returnDateTimeStamp:Long? =null,
    val returnDate:String="",
    val isTravelPurchased:Boolean = false,
    val isLoading:Boolean=false,
    val differenceInDays:Int = 0,
    val showDepatureDatePicker:Boolean = false,
    val showReturnDatePicker:Boolean = false,
    val currentLocation:String = "Dhaka, Bangladesh",
    val formattedDepatureDate:String="",
    val formattedReturnDate:String="",
    val showWelcomeToNewLocationSheet:Boolean=false,
    val airports : List<Airport> = emptyList()
)

@Serializable
data class Airport(
    val country:String,
    val city:String,
    @SerialName("airport_name")
    val airportName:String
)