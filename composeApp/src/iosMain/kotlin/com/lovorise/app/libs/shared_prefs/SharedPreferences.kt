package com.lovorise.app.libs.shared_prefs

import coil3.PlatformContext
import platform.Foundation.NSUserDefaults

//@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SharedPrefsImpl actual constructor(context: PlatformContext?) : SharedPrefs {

    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults()


    override fun getString(key: String, defaultValue: String?): String? {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun setString(key: String, value: String?) {
        userDefaults.setObject(value, forKey = key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return userDefaults.boolForKey(key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, forKey = key)
    }
}