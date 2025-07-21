package com.lovorise.app.settings.presentation.screens.travel_ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.i_am_travelling_to
import coinui.composeapp.generated.resources.ic_travel_aeroplane
import coinui.composeapp.generated.resources.no_result_for_travel
import coinui.composeapp.generated.resources.search_and_set_travel
import coinui.composeapp.generated.resources.select_destination
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class SelectDestinationScreen : Screen {


    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val travelTicketScreenModel = navigator.koinNavigatorScreenModel<TravelTicketScreenModel>()

        val state by travelTicketScreenModel.state.collectAsState()
        SelectDestinationScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            onSelected = {
                travelTicketScreenModel.updateSelectedDestination(it)
                navigator.pop()
            },
            state = state
        )
    }
}

@Composable
fun SelectDestinationScreenContent(isDarkMode:Boolean,goBack:()->Unit,onSelected:(Airport)->Unit,state: TravelScreenState) {

    var searchQuery by rememberSaveable { mutableStateOf("") }


    var searchResults by rememberSaveable{ mutableStateOf(emptyList<Airport>()) }



    LaunchedEffect(searchQuery){
        if (searchQuery.isNotBlank()){
            // delay(100L)
            val filteredItems = state.airports.filter { item ->
                item.country.lowercase().startsWith(searchQuery.lowercase()) ||
                item.city.lowercase().startsWith(searchQuery.lowercase()) ||
                item.airportName.lowercase().startsWith(searchQuery.lowercase())
            }
            searchResults = filteredItems
        }else{
            searchResults = emptyList()
        }
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

            HeaderWithTitleAndBack(title = stringResource(Res.string.select_destination), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                SearchTextField(
                    label = stringResource(Res.string.i_am_travelling_to),
                    onQueryChange = { searchQuery = it },
                    query = searchQuery,
                    roundedCornerPercent = 23,
                    isDarkMode = isDarkMode
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)){
                if (searchQuery.isNotBlank() && searchResults.isEmpty()) {
                    Box(Modifier.fillMaxWidth().fillMaxHeight(0.85f), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.no_result_for_travel),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 20.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            textAlign = TextAlign.Center
                        )
                    }
                }else if (searchQuery.isBlank()){
                    Box(Modifier.fillMaxWidth().fillMaxHeight(0.85f), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.search_and_set_travel),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 20.sp,
                            color = if (isDarkMode) Color.White else Color(0xff101828),
                            textAlign = TextAlign.Center
                        )
                    }
                }else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                          verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchResults) { item ->
                            TravelSearchItem(item, onClick = {onSelected(item)}, isDarkMode = isDarkMode)
                        }
                    }
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

    

}

@Composable
fun TravelSearchItem(airport: Airport,onClick:(Airport)->Unit,isDarkMode:Boolean){

    Column(Modifier.noRippleClickable { onClick(airport) }) {
        Text(
            text = "${airport.city}, ${airport.country}",
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) Color.White else Color(0xff101828),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp
        )
        Spacer(Modifier.height(8.dp))
        Row {

            Icon(
                imageVector = vectorResource(Res.drawable.ic_travel_aeroplane),
                contentDescription = null,
                tint = if(isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = airport.airportName,
                fontFamily = PoppinsFontFamily(),
                color = if(isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp
            )
        }

    }
}


