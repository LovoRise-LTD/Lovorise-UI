package com.lovorise.app.settings.presentation.screens.travel_ticket

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import coinui.composeapp.generated.resources.Res
import com.lovorise.app.accounts.domain.model.AppSettingsData
import com.lovorise.app.accounts.presentation.AccountsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

class TravelTicketScreenModel(private val accountsViewModel: AccountsViewModel) : ScreenModel {

    private val _state = MutableStateFlow(TravelScreenState())

    val state = _state.asStateFlow()

    init {
        loadAllAirport()
    }

    @OptIn(ExperimentalResourceApi::class)
    fun loadAllAirport(){
        if (state.value.airports.isNotEmpty()) return

        screenModelScope.launch {
            val airports = Json.decodeFromString<List<Airport>>( Res.readBytes("files/airports.json").decodeToString())
            _state.update {
                it.copy(
                    airports = airports
                )
            }
        }
        loadDataFromApi()
    }

    private fun loadDataFromApi(){
        val data = accountsViewModel.state.value.appSettingsData?.travelTicketStatus ?: return
        if (!data.enabled) return

        val departDate: LocalDate = try { LocalDate.parse(data.departDate) }catch (e:Exception){ null } ?: return
        val returnDate: LocalDate = try { LocalDate.parse(data.returnDate) }catch (e:Exception){ null } ?: return


        _state.update {
            it.copy(
                selectedDestination = Airport(city = data.destination.city, country = data.destination.country, airportName = ""),
                returnDate = data.returnDate.replace("-","/"),
                depatureDate = data.departDate.replace("-","/"),
                isTravelPurchased = data.enabled,
                formattedReturnDate = returnDate.toFormattedDate(),
                formattedDepatureDate = departDate.toFormattedDate(),
                depatureDateTimeStamp = departDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                returnDateTimeStamp = returnDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
            )
        }
    }



    fun updateSelectedDestination(value:Airport){
        _state.update {
            it.copy(
                selectedDestination = value
            )
        }
    }

    fun updateShowWelcomeBottomSheetState(value: Boolean){
        _state.update {
            it.copy(
                showWelcomeToNewLocationSheet = value
            )
        }
    }



    fun showDepatureDatePicker(value:Boolean){
        _state.update {
            it.copy(
                showDepatureDatePicker = value
            )
        }
    }

    fun updateTicketPurchase(context:PlatformContext){
        val airport = state.value.selectedDestination ?: return
        screenModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = accountsViewModel.createTravelTicket(context, AppSettingsData.TravelTicketStatus(
                enabled = false,
                returnDate = state.value.returnDate.replace("/","-"),
                departDate = state.value.depatureDate.replace("/","-"),
                destination = AppSettingsData.TravelTicketStatus.Destination(
                    city = airport.city,
                    country = airport.country,
                    longitude = 0.0,
                    latitude = 0.0
                )
            ))
            _state.update {
                it.copy(
                    isTravelPurchased = result,
                    isLoading = false
                )
            }
        }

    }

    fun showReturnDatePicker(value:Boolean){
        _state.update {
            it.copy(
                showReturnDatePicker = value
            )
        }
    }

    private fun updateDifferenceInDays(){
        val timeZone = TimeZone.currentSystemDefault()

        if (state.value.depatureDateTimeStamp == null || state.value.returnDateTimeStamp == null) return

        val depatureInstant = Instant.fromEpochMilliseconds(state.value.depatureDateTimeStamp!!).toLocalDateTime(timeZone).date
        val returnInstant = Instant.fromEpochMilliseconds(state.value.returnDateTimeStamp!!).toLocalDateTime(timeZone).date

        _state.update {
            it.copy(
                differenceInDays = depatureInstant.daysUntil(returnInstant),
                formattedReturnDate = returnInstant.toFormattedDate(),
                formattedDepatureDate = depatureInstant.toFormattedDate()
            )
        }

    }

    private fun LocalDate.toFormattedDate():String{
        return "${this.dayOfMonth} ${this.month.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0, 3)}, ${this.year}"
    }

    fun updateReturnDate(value:Long?){
        if (value == null) return
        val selectedDate = Instant.fromEpochMilliseconds(value)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        _state.update {
            it.copy(
                returnDateTimeStamp = value,
                returnDate = "${selectedDate.year}/${selectedDate.monthNumber.toString().padStart(2, '0')}/${selectedDate.dayOfMonth}"
            )
        }
        updateDifferenceInDays()

    }

    fun updateDepatureDate(value:Long?){
        if (value == null) return
        val selectedDate = Instant.fromEpochMilliseconds(value)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        _state.update {
            it.copy(
                depatureDateTimeStamp = value,
                depatureDate = "${selectedDate.year}/${selectedDate.monthNumber.toString().padStart(2, '0')}/${selectedDate.dayOfMonth}"
            )
        }
        updateDifferenceInDays()
    }



}