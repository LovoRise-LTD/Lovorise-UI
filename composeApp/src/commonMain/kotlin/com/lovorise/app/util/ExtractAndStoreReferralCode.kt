package com.lovorise.app.util

import coil3.PlatformContext
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl

fun extractAndStoreReferralCodeFromUrl(url: String,context: PlatformContext?) {
    val query = url.substringAfter("?", "")
    val code = query
        .split("&")
        .mapNotNull {
            val (key, value) = it.split("=")
                .map { part -> part.trim() }
                .let { list -> if (list.size == 2) list else return@mapNotNull null }
            key to value
        }
        .toMap()["referral-code"]

    if (!code.isNullOrBlank()){
        if (isAndroid() && context == null) throw Exception("Context cannot be null")
        val prefs = SharedPrefsImpl(context)
        prefs.setString(PreferencesKeys.REFERRAL_CODE,code)
        println("Referral code: $code")
    }
}