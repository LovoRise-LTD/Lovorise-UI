package com.lovorise.app.onboarding_info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily

@Composable
fun DescriptionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    bgColor: Color = Color.White,
    borderColor:Color = Color(0XFFD0D5DD),
    imeAction: ImeAction = ImeAction.Done,
    onSend:(()->Unit)? = null,
    cornerRadiusPercent : Int = 8,
    enabled:Boolean = true,
    height:Dp = 177.dp,
    label: String? = null,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = Color.Black),
    labelTextStyle: TextStyle = TextStyle(
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        color = Color(0xff667085)
    ),
    cursorColor:Color = Color.Black
) {

    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadiusPercent)
            )
            .background(bgColor, shape = RoundedCornerShape(cornerRadiusPercent))
    ) {

        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 12.dp)) {
            BasicTextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = textStyle,
                cursorBrush = SolidColor(cursorColor),
                singleLine = singleLine,
                maxLines = maxLines,
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onSend?.let { it() }
                        keyboardController?.hide()
                    }
                ),
                enabled = enabled
            )

            if (value.isEmpty() && !isFocused && label != null) {
                Text(
                    text = label,
                    fontFamily = labelTextStyle.fontFamily,
                    fontWeight = labelTextStyle.fontWeight,
                    fontSize = labelTextStyle.fontSize,
                    lineHeight = labelTextStyle.lineHeight,
                    letterSpacing = labelTextStyle.letterSpacing,
                    color = labelTextStyle.color
                )
            }
        }
    }

}