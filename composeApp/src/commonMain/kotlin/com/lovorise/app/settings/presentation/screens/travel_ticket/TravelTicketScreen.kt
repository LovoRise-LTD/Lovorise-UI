package com.lovorise.app.settings.presentation.screens.travel_ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.current_location
import coinui.composeapp.generated.resources.days
import coinui.composeapp.generated.resources.for_the_next
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.select_depature_date
import coinui.composeapp.generated.resources.select_destination
import coinui.composeapp.generated.resources.select_return_date
import coinui.composeapp.generated.resources.travel
import coinui.composeapp.generated.resources.travel_ticket
import coinui.composeapp.generated.resources.use_travel_ticket_to_change
import coinui.composeapp.generated.resources.your_travel_ticket_destination_set_to
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.home.HomeScreen
import com.lovorise.app.home.TabsScreenModel
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

class TravelTicketScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val tabsScreenModel = navigator.koinNavigatorScreenModel<TabsScreenModel>()

        val screenModel = navigator.koinNavigatorScreenModel<TravelTicketScreenModel>()

        val state by screenModel.state.collectAsState()


        TravelTicketScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            navigateToSelectDestinationScreen = {
                navigator.push(SelectDestinationScreen())
            },
            state = state,
            screenModel = screenModel,
            navigateToHome = {
                tabsScreenModel.updateTab(TabsScreenModel.BottomTab.SWIPE)
                navigator.popUntil {
                    it.instanceOf(HomeScreen::class)
                }
            },
            accountsViewModel = accountsViewModel
        )
    }
}

enum class SelectDestinationType{
    CURRENT,DESTINATION
}

@OptIn(ExperimentalMaterial3Api::class)
class FutureSelectableDates(private val greaterThanMillis:Long,private val lessThanMillis:Long?=null) : SelectableDates{
    // Get the current date and time
    private val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())


    // Calculate the instant 24 hours from now
    private val oneDayAheadMillis =  now.date.plus(1, DateTimeUnit.DAY)
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()


    @ExperimentalMaterial3Api
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
     //   println("the isSelectable is ${utcTimeMillis >= (greaterThanMillis ?: oneDayAheadMillis)}")
       // if (lessThanMillis != null) return utcTimeMillis in greaterThanMillis..lessThanMillis

        val greaterThan = Instant.fromEpochMilliseconds(greaterThanMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
        val lessThan = lessThanMillis?.let {  Instant.fromEpochMilliseconds(lessThanMillis) }?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
        val selected = Instant.fromEpochMilliseconds(utcTimeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (lessThan != null) return selected in greaterThan..< lessThan

        return selected >= greaterThan
    }


    @ExperimentalMaterial3Api
    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelTicketScreenContent(screenModel: TravelTicketScreenModel,state: TravelScreenState,isDarkMode:Boolean,goBack:()->Unit,navigateToSelectDestinationScreen:()->Unit,navigateToHome:()->Unit,accountsViewModel: AccountsViewModel) {


    val accountsState by accountsViewModel.state.collectAsState()


    var isEnabled by remember { mutableStateOf(false) }



    var depatureDatePickerState = rememberDatePickerState(
        selectableDates = FutureSelectableDates(
            greaterThanMillis = (Clock.System.now().toEpochMilliseconds())
        )
    )
    // Return date state, which depends on the selected departure date
    var returnDatePickerState = rememberDatePickerState(
        selectableDates = FutureSelectableDates(
            greaterThanMillis = depatureDatePickerState.selectedDateMillis ?: Clock.System.now()
                .toEpochMilliseconds()
        )
    )


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val context = LocalPlatformContext.current

    LaunchedEffect(true){
        state.depatureDateTimeStamp?.let { depatureDatePickerState.selectedDateMillis = it }
        state.returnDateTimeStamp?.let { returnDatePickerState.selectedDateMillis = it }
    }



    LaunchedEffect(state.currentLocation,state.selectedDestination,state.depatureDate,state.returnDate){
        isEnabled = state.currentLocation.isNotBlank() && state.selectedDestination != null && state.depatureDate.isNotBlank() && state.returnDate.isNotBlank() && (state.depatureDate != state.returnDate) && (state.depatureDateTimeStamp!! < state.returnDateTimeStamp!!)
    }


    LaunchedEffect(state.isTravelPurchased){
        if (state.isTravelPurchased){
            screenModel.updateShowWelcomeBottomSheetState(true)
        }
    }

    if (state.showDepatureDatePicker){
        depatureDatePickerState = rememberDatePickerState(
            selectableDates = FutureSelectableDates(
                greaterThanMillis = (Clock.System.now().toEpochMilliseconds()),
                lessThanMillis = state.returnDateTimeStamp
            )
        )
        state.depatureDateTimeStamp?.let { depatureDatePickerState.selectedDateMillis = it }
    }


    if (state.showReturnDatePicker){
        returnDatePickerState = rememberDatePickerState(
            selectableDates = FutureSelectableDates(
                greaterThanMillis = (state.depatureDateTimeStamp ?: Clock.System.now().toEpochMilliseconds())+86400000
            )
        )
        state.returnDateTimeStamp?.let { returnDatePickerState.selectedDateMillis = it }
    }


    Column(modifier = Modifier) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderWithTitleAndBack(title = stringResource(Res.string.travel_ticket), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Column (modifier = Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState())){
                Text(
                    text = if (!state.isTravelPurchased) stringResource(Res.string.use_travel_ticket_to_change) else "${stringResource(Res.string.your_travel_ticket_destination_set_to)} Delhi ${stringResource(Res.string.for_the_next)} ${state.differenceInDays} ${stringResource(Res.string.days)}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(24.dp))

                TravelTicketItem(
                    label = stringResource(Res.string.current_location),
                    value = "${accountsState.currentLocation?.city}, ${accountsState.currentLocation?.country}",
                    onClick = {},
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                TravelTicketItem(
                    label = stringResource(Res.string.select_destination),
                    value = if (state.selectedDestination == null) "" else state.selectedDestination.let { "${it.city}, ${it.country}" },
                    onClick = navigateToSelectDestinationScreen,
                    isDarkMode = isDarkMode
                )

                if (!state.isTravelPurchased) {

                    Spacer(Modifier.height(16.dp))

                    TravelTicketItem(
                        label = stringResource(Res.string.select_depature_date),
                        value = state.depatureDate,
                        onClick = {
                            screenModel.showDepatureDatePicker(true)
                        },
                        isDarkMode = isDarkMode
                    )

                    Spacer(Modifier.height(16.dp))

                    TravelTicketItem(
                        label = stringResource(Res.string.select_return_date),
                        value = state.returnDate,
                        onClick = {
                            screenModel.showReturnDatePicker(true)
                        },
                        isDarkMode = isDarkMode
                    )


                    Spacer(Modifier.height(32.dp))

                    ButtonWithText(
                        text = stringResource(Res.string.travel),
                        bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                        textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                        onClick = {
                            if (isEnabled) {
                                screenModel.updateTicketPurchase(context)
                                //navigateToDeleteFlow2()
                            }
                        }
                    )
                }

            }


        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )

    }

    if (state.isLoading){
        CircularLoader()
    }

    if (state.showReturnDatePicker || state.showDepatureDatePicker) {
        Dialog(
            onDismissRequest = {
                if(state.showDepatureDatePicker) {
                    screenModel.showDepatureDatePicker(false)
                }
                if(state.showReturnDatePicker) {
                    screenModel.showReturnDatePicker(false)
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 17.dp).wrapContentSize()
                    .background(if (isDarkMode) CARD_BG_DARK else Color.White,RoundedCornerShape(20.dp))
            ) {


                Column {

                    DatePicker(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        state = if (state.showReturnDatePicker) returnDatePickerState else depatureDatePickerState,
                        colors = DatePickerDefaults.colors(
                            disabledYearContentColor = if (isDarkMode) DISABLED_TEXT_DARK else Color.Unspecified,
                            yearContentColor = if (isDarkMode) Color.White else Color.Unspecified,
                            disabledDayContentColor = if (isDarkMode) DISABLED_TEXT_DARK else Color.Unspecified,
                            dayContentColor = if (isDarkMode) Color.White else Color.Unspecified,
                            titleContentColor = if (isDarkMode) Color.White else Color(0xff101828),
                            weekdayContentColor =  if (isDarkMode) Color.White else Color(0xff101828),
                            headlineContentColor = if (isDarkMode) Color.White else Color(0xff101828),
                            todayContentColor = Color(0xffF33358),
                            todayDateBorderColor = Color(0xffF33358),
                            selectedDayContainerColor = Color(0xffF33358),
                            selectedDayContentColor = Color.White,
                            selectedYearContentColor = Color.White,
                            selectedYearContainerColor = Color(0xffF33358),
                            containerColor = if (isDarkMode) CARD_BG_DARK else Color.White,
                            dayInSelectionRangeContentColor = Color(0xffF33358),
                            navigationContentColor = Color(0xffF33358),
                            dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                                focusedLabelColor = Color(0xffF33358),
                                unfocusedLabelColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085),
                                focusedBorderColor = Color(0xffF33358),
                                unfocusedBorderColor = Color(0xffD0D5DD),
                                cursorColor = Color(0xffF33358),
                                focusedTextColor = if (isDarkMode) Color.White else Color(0xff101828),
                                unfocusedTextColor =if (isDarkMode) Color.White else Color(0xff101828),
                                selectionColors = TextSelectionColors(
                                    handleColor = Color(0xffF33358),
                                    backgroundColor = Color(0xffF33358).copy(alpha = 0.3f)
                                )
                            )
                        )
                    )

                    Row(
                        Modifier.fillMaxWidth().height(38.dp)
                    ) {

                        Spacer(Modifier.weight(1f))

                        Text(
                            modifier = Modifier.noRippleClickable {
                                screenModel.showDepatureDatePicker(false)
                                screenModel.showReturnDatePicker(false)
                            },
                            text = stringResource(Res.string.cancel),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.14.sp,
                            lineHeight = 18.78.sp,
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.09.sp,
                            color = Color(0xffF33358)
                        )


                        Spacer(Modifier.width(30.dp))


                        Text(
                            modifier = Modifier.noRippleClickable {
                                if(state.showReturnDatePicker){
                                    screenModel.updateReturnDate(returnDatePickerState.selectedDateMillis)
                                    screenModel.showReturnDatePicker(false)
                                }
                                if (state.showDepatureDatePicker) {
                                    screenModel.updateDepatureDate(depatureDatePickerState.selectedDateMillis)
                                    screenModel.showDepatureDatePicker(false)
                                }

                            },
                            text = stringResource(Res.string.ok),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.14.sp,
                            lineHeight = 18.78.sp,
                            fontFamily = PoppinsFontFamily(),
                            letterSpacing = 0.09.sp,
                            color = Color(0xffF33358)
                        )

                        Spacer(Modifier.width(22.54.dp))


                    }
                }
            }
        }
    }

    if (state.showWelcomeToNewLocationSheet) {
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                screenModel.updateShowWelcomeBottomSheetState(false)
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            WelcomeToNewLocationBottomSheetContent(
                isDarkMode = isDarkMode,
                onStartExploring = {
                    sheetState.hideWithCompletion(scope){
                        screenModel.updateShowWelcomeBottomSheetState(false)
                        navigateToHome()
                    }
                },
                startDate = state.formattedDepatureDate,
                endDate = state.formattedReturnDate,
                newLocation =  if (state.selectedDestination == null) "" else state.selectedDestination.let { "${it.city}, ${it.country}" }
            )

        }
    }
    

}


@Composable
fun TravelTicketItem(label:String,value:String,onClick:()->Unit,isDarkMode: Boolean) {

    Box(
        Modifier.fillMaxWidth()
            .height(56.dp)
            .border(
                width = 1.5.dp,
                color = Color(0xffD0D5DD),
                shape = RoundedCornerShape(8.dp)
            ).noRippleClickable(onClick),
        contentAlignment = Alignment.CenterStart
    ){

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = value.ifBlank { label },
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = PoppinsFontFamily(),
            letterSpacing = 0.2.sp,
            color = if (value.isBlank()) Color(0xff667085) else {
                if (isDarkMode) Color.White else Color(0xff101828)
            }
        )

    }
    
}



