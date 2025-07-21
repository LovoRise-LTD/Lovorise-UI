package com.lovorise.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomDivider(
    color: Color? = null,
    isDarkMode:Boolean = false
) {

    Box(Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color ?: (if (isDarkMode) Color(0xff737272) else Color(0xffEAECF0)))
    )

}