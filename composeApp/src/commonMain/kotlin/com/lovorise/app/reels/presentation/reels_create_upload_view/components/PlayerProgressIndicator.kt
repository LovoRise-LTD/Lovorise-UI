package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProgressIndicator(progress: Float){

    Slider(
        enabled = false,
        value = progress,
        onValueChange = {},
        colors = SliderDefaults.colors(
            disabledActiveTickColor = Color.Transparent,
            disabledInactiveTickColor = Color.Transparent,
            inactiveTickColor = Color.Transparent,
            activeTrackColor = Color.Transparent,
            inactiveTrackColor = Color.Transparent,
            thumbColor = Color.Transparent,
            disabledActiveTrackColor = Color.Transparent,
            disabledInactiveTrackColor = Color.Transparent
        ),
        thumb = {
            Box(Modifier.width(1.dp).height(36.dp).background(Color.White))
        }

    )
}