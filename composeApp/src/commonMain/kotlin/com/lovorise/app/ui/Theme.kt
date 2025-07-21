package com.lovorise.app.ui

import cafe.adriel.voyager.core.model.ScreenModel
import coil3.PlatformContext
import com.lovorise.app.libs.shared_prefs.PreferencesKeys
import com.lovorise.app.libs.shared_prefs.SharedPrefs
import com.lovorise.app.libs.shared_prefs.SharedPrefsImpl
import com.lovorise.app.updateThemeStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


enum class ThemeType{
    AUTO,LIGHT,DARK
}


class ThemeViewModel : ScreenModel{

   // private lateinit var prefs: SharedPrefs

    private var _currentThemeType = MutableStateFlow(ThemeType.LIGHT)
    val currentThemeType = _currentThemeType.asStateFlow()



    fun init(context:PlatformContext){
//        prefs = SharedPrefsImpl(context)
//        _currentThemeType.update{ ThemeType.valueOf(prefs.getString(PreferencesKeys.CURRENT_THEME,"LIGHT") ?: "LIGHT")}
    }

    fun updateTheme(theme: ThemeType,context: PlatformContext,isSystemDark: Boolean){

//        _currentThemeType.update{ theme }
//        val isDark =  when (theme) {
//            ThemeType.AUTO -> isSystemDark
//            ThemeType.LIGHT -> false
//            ThemeType.DARK -> true
//        }
//        updateThemeStyle(isDarkMode(isDark),context)
//        prefs.setString(PreferencesKeys.CURRENT_THEME,theme.name)
    }

    fun isDarkMode(isSystemDark: Boolean) : Boolean{
        return when (currentThemeType.value) {
            ThemeType.AUTO -> isSystemDark
            ThemeType.LIGHT -> false
            ThemeType.DARK -> true
        }
    }

}