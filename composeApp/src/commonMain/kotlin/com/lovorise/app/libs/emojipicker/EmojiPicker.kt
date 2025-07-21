package com.lovorise.app.libs.emojipicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun EmojiPicker(onPicked:(String)->Unit,height: Dp)