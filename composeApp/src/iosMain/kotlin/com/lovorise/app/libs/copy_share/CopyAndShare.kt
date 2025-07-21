package com.lovorise.app.libs.copy_share

import coil3.PlatformContext
import platform.UIKit.UIPasteboard
import platform.Foundation.NSArray
import platform.Foundation.arrayWithObject
import platform.UIKit.*

actual fun copyToClipboard(text: String,context:PlatformContext) {
    UIPasteboard.generalPasteboard.string = text
}



actual fun shareText(text: String,context: PlatformContext) {
    val activityController = UIActivityViewController(
        activityItems = NSArray.arrayWithObject(text),
        applicationActivities = null
    )


    // Modern way to get the key window
    val keyWindow = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { it.windows }
        .firstOrNull() as? UIWindow

    val rootViewController = keyWindow?.rootViewController

    // Safely present viewController
    rootViewController?.presentViewController(
        activityController,
        animated = true,
        completion = null
    ) ?: println("Error: rootViewController is null")

}