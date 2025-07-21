package com.lovorise.app.libs

import coil3.PlatformContext
import platform.SafariServices.*
import platform.UIKit.*
import platform.Foundation.*

actual fun openUrlInCustomTab(url: String, context: PlatformContext) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    val safariVC = SFSafariViewController(nsUrl)

    val keyWindow = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { it.windows }
        .firstOrNull() as? UIWindow

    val rootViewController = keyWindow?.rootViewController

    rootViewController?.presentViewController(
        safariVC,
        animated = true,
        completion = null
    ) ?: println("Error: Cannot present SafariViewController")
}
