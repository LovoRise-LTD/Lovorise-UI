package com.lovorise.app.libs

import coil3.PlatformContext
import platform.StoreKit.SKStoreReviewController
import platform.UIKit.*
import platform.darwin.*

actual fun rateApp(context: PlatformContext, onSuccess:()->Unit, onFailed:()->Unit){
    val delayTime = dispatch_time(DISPATCH_TIME_NOW, 1 * NSEC_PER_SEC.toLong())
    dispatch_after(delayTime, dispatch_get_main_queue()) {
        val scene = UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }

        scene?.let {
            SKStoreReviewController.requestReviewInScene(it)
        }
    }
}