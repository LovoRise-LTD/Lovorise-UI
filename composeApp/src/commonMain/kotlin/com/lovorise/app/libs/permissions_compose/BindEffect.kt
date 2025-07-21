package com.lovorise.app.libs.permissions_compose

import androidx.compose.runtime.Composable
import com.lovorise.app.libs.permissions.PermissionsController

@Suppress("FunctionNaming")
@Composable
expect fun BindEffect(permissionsController: PermissionsController)
