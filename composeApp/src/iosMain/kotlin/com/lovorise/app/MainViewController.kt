package com.lovorise.app

import androidx.compose.ui.window.ComposeUIViewController
import com.lovorise.app.libs.iap.InAppPurchasesAndSubscriptions
import com.lovorise.app.util.CacheUtil
import com.lovorise.app.util.GpsProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

//onFocusBehavior = OnFocusBehavior.DoNothing

@OptIn(ExperimentalForeignApi::class)
fun MainViewController(mediaPlayerWorker: MediaPlayerWorker,cacheUtil: CacheUtil,iap:InAppPurchasesAndSubscriptions) = ComposeUIViewController {
   // val screen = UIScreen.mainScreen
//    val scale = screen.scale.toFloat()

//    val screenWidthDp = (screen.bounds.useContents { size.width.toFloat() })
//
//    val screenHeightDp = (screen.bounds.useContents { size.height.toFloat() })
    App(
        mediaPlayerWorker = mediaPlayerWorker,
        gpsProvider = IOSGpsProvider(),
        cacheUtil = cacheUtil,
        iap = iap
    )

}

private fun setStatusBarColor(color: Long) {
    val uiColor = UIColor(
        red = ((color and 0xFF0000) shr 16).toDouble() / 255.0,
        green = ((color and 0xFF00) shr 8).toDouble() / 255.0,
        blue = (color and 0xFF).toDouble() / 255.0,
        alpha = 1.0
    )

    val window = UIApplication.sharedApplication.keyWindow
    window?.rootViewController?.view?.backgroundColor = uiColor

    // Optionally, set the status bar style
    if (color < 0xFF888888) {
        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleLightContent)
    } else {
        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleDarkContent)
    }
}

class IOSGpsProvider:GpsProvider{
    override fun isGpsEnabled(): Boolean {
        return true
    }

    override fun promptEnableGPS(onResult: (Boolean) -> Unit) {
        onResult(true)
    }

    override fun startGpsListener() {

    }

    override val currentGpsState: StateFlow<Boolean>
        get() = MutableStateFlow(true)

    override fun stopGpsListener() {

    }
}