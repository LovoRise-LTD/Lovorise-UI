package com.lovorise.app.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import coil3.PlatformContext
import com.lovorise.app.GpsProviderInstance
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.libs.location.LocationManager
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TabsScreenModel(
    private val accountsViewModel: AccountsViewModel
) : KoinComponent, ScreenModel {

    private val _state = MutableStateFlow(TabsState())
    val state = _state.asStateFlow()
    private var isLocationRequested = false


    fun onInit(context: PlatformContext){

        screenModelScope.launch {
            val prefs = SharedPrefsImpl(context)
            //val isShown = prefs.getBoolean(PreferencesKeys.IS_GUIDELINES_SHOWN,false)
            _state.update {
                it.copy(
                 //   isGuideLinesShown = isShown
                )
            }
        }
    }

    fun updateLocation(locationPermissionState: PermissionState,ctx: PlatformContext,showEnableGpsPrompt : ()->Unit){
        screenModelScope.launch {
            val loc = accountsViewModel.state.value.currentLocation
            if (locationPermissionState == PermissionState.Granted && !isLocationRequested){
                if (GpsProviderInstance.DEFAULT?.isGpsEnabled() == true){
                    val locationManager = LocationManager(ctx)
                    locationManager.getCurrentLocation().collectLatest {data->
                        if (!data.isNull()) {
                            isLocationRequested = true
                            accountsViewModel.updateCurrentLocation(data!!)
                            println("the current location is $data")
                        }
                    }
                }else{
                    showEnableGpsPrompt()
                }
            }
        }
    }

    fun setGuidelinesState(context: PlatformContext){
        screenModelScope.launch {
            val prefs = SharedPrefsImpl(context)
            //prefs.setBoolean(PreferencesKeys.IS_GUIDELINES_SHOWN,true)
            _state.update {
                it.copy(
                    isGuideLinesShown = true
                )
            }
        }
    }


    fun updateTab(tab:BottomTab){
        _state.update {
            it.copy(
                activeTab = tab,
                navStack = state.value.navStack.toMutableList().apply {
                    add(tab)
                }
            )
        }
    }

    fun updateStack(stack:List<BottomTab>){
        if (stack.isEmpty()) return
        _state.update{
            it.copy(
                navStack = stack,
                activeTab = stack.last()
            )
        }
    }

    fun loadData1(ctx:PlatformContext,navigateToSignIn:()->Unit){
        if (accountsViewModel.state.value.user == null){
            accountsViewModel.getUser(ctx,navigateToSignIn)
        }
    }

    enum class BottomTab{
        SWIPE,EVENTS,REELS,CHAT,PROFILE
    }
}

