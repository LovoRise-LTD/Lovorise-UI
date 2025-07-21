package com.lovorise.app

import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import coil3.PlatformContext
import io.ktor.http.content.OutgoingContent
import org.koin.core.module.Module

interface Platform {
    val name: String
}


expect fun pxToSp(px: Float,density: Density): TextUnit

expect suspend fun bodyFromFile(filepath: String): OutgoingContent

expect fun getPlatform(): Platform

expect fun isAndroid():Boolean

expect fun getAppVersion(context: PlatformContext): String

expect fun getDeviceName(): String

expect fun getDatabaseModule(): Module


expect fun disableUiKitOverscroll()
expect fun provideNullAndroidOverscrollConfiguration(): Array<ProvidedValue<*>>

//expect fun hasSystemUINavigation(): Boolean

expect fun closeApp(context: PlatformContext)

expect fun formatToTwoDecimalPlaces(value: Float): String

expect fun updateThemeStyle(isDark:Boolean,context: PlatformContext)

expect fun showPermissionScreen(context: PlatformContext):Boolean

