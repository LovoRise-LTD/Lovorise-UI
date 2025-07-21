package com.lovorise.app.accounts.presentation.signup.notification

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lovorise.app.libs.permissions.DeniedAlwaysException
import com.lovorise.app.libs.permissions.DeniedException
import com.lovorise.app.libs.permissions.Permission
import com.lovorise.app.libs.permissions.PermissionState
import com.lovorise.app.libs.permissions.PermissionsController
import com.lovorise.app.libs.permissions.RequestCanceledException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationPermissionScreenModel(
    val controller: PermissionsController
) : ScreenModel{

    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState = _permissionState.asStateFlow()

    init {
        updatePermissionState()
    }

    suspend fun isNotificationPermissionGranted():Boolean{
        return controller.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
    }

    private fun updatePermissionState(){
        screenModelScope.launch {
            _permissionState.update {
               val s =  controller.getPermissionState(Permission.REMOTE_NOTIFICATION)
                println("the permission state is $s")
                s
            }
        }
    }

    fun provideOrRequestPermission(){
        screenModelScope.launch {
            _permissionState.update {
                try {
                    controller.providePermission(Permission.REMOTE_NOTIFICATION)
                    println("the location permission is granted!!")
                    PermissionState.Granted
                } catch (e: DeniedAlwaysException) {
                    PermissionState.DeniedAlways
                } catch (e: DeniedException) {
                    PermissionState.Denied
                } catch (e: RequestCanceledException) {
                    PermissionState.NotDetermined
                }
            }
        }
    }

    fun openSettings(){
        controller.openAppSettings()
    }

}