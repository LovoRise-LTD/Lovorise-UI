package com.lovorise.app.libs.permissions_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.lovorise.app.libs.permissions.ios.PermissionsController

@Composable
actual fun rememberPermissionsControllerFactory(): PermissionsControllerFactory {
    return remember {
        PermissionsControllerFactory {
            PermissionsController()
        }
    }
}
