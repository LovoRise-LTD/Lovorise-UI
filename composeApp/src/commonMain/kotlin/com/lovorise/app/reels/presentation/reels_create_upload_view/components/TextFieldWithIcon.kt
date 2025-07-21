package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TextFieldWithIcon(
    label:String,
    value:String,
    onTextFieldValueChange:(String)->Unit,
    cornerRadius: Dp = 8.dp,
    cursorColor: Brush = SolidColor(Color(0xffF33358)),
    icon: DrawableResource,
    backgroundColor: Color = Color(0xffF2F4F7).copy(alpha = 0.6f),
    horizontalPadding: Dp = 16.dp,
) {



    Box(
        modifier = Modifier
            //   .imePadding()
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding)
            .height(40.dp)
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = vectorResource(icon),
                contentDescription = "Icon",
                modifier = Modifier.size(20.dp),
                tint = Color(0xff475467)
            )
            BasicTextField(
                cursorBrush = cursorColor,
                value = value,
                onValueChange = onTextFieldValueChange,
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth().weight(1f),
                decorationBox = { padding ->
                    if (value.isBlank()) {
                        Text(
                            text = label,
                            color = Color(0xff475467),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                    padding()
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily()
                )
            )
        }
    }
}