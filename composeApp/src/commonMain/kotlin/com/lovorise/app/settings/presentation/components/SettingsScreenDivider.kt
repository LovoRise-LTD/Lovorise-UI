package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lovorise.app.ui.CARD_BG_DARK


@Composable
fun SettingsScreenDivider(isDarkMode:Boolean) {
    Box(Modifier.fillMaxWidth().height(8.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))
}