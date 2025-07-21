package com.lovorise.app.libs.shared_prefs

import coil3.PlatformContext

interface SharedPrefs {
    fun getString(key: String, defaultValue: String? = null): String?
    fun setString(key: String, value: String?)
    fun getBoolean(key: String,defaultValue: Boolean): Boolean
    fun setBoolean(key: String,value: Boolean)
}

expect class SharedPrefsImpl(context: PlatformContext?) :SharedPrefs