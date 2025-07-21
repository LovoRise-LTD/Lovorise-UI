package com.lovorise.app.libs.permissions_compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.lovorise.app.libs.permissions.PermissionsController

@Suppress("FunctionNaming")
@Composable
actual fun BindEffect(permissionsController: PermissionsController) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context: Context = LocalContext.current

    LaunchedEffect(permissionsController, lifecycleOwner, context) {
        val activity: ComponentActivity = checkNotNull(context as? ComponentActivity) {
            "$context context is not instance of ComponentActivity"
        }

        permissionsController.bind(activity)
    }
}
