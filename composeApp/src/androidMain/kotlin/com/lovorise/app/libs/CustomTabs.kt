package com.lovorise.app.libs

import android.app.Activity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import coil3.PlatformContext


actual fun openUrlInCustomTab(url: String, context: PlatformContext){
    val activity = context as? Activity ?: return

    val customTabsIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(activity, url.toUri())
}
