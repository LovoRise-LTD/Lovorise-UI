package com.lovorise.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable

@Composable
fun ButtonWithText(
    modifier: Modifier = Modifier,
    text:String,
    bgColor:Color,
    textColor: Color,
    height:Dp = 40.dp,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(40)
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(bgColor, shape = shape)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }


}