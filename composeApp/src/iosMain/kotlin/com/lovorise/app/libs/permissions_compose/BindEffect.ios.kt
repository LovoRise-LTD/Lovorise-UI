package com.lovorise.app.libs.permissions_compose

import androidx.compose.runtime.Composable
import com.lovorise.app.libs.permissions.PermissionsController

// on iOS side we should not do anything to prepare PermissionsController to work
@Suppress("FunctionNaming")
@Composable
actual fun BindEffect(permissionsController: PermissionsController) = Unit
