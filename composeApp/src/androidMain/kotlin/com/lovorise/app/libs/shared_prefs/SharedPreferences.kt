package com.lovorise.app.libs.shared_prefs

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import coil3.PlatformContext

actual class SharedPrefsImpl actual constructor(context: PlatformContext?) : SharedPrefs {

    private val prefs: SharedPreferences

    init {
        // Ensure the context is not null
        val nonNullContext = context ?: throw IllegalArgumentException("SharedPrefsImpl cannot be created with a null context for Android")
        prefs = nonNullContext.getSharedPreferences(PrefsFileName, MODE_PRIVATE)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }

    override fun setString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key,defaultValue)
    }

    override fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key,value).apply()
    }
}

const val PrefsFileName = "AppPrefs"