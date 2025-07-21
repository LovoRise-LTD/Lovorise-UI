package com.lovorise.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CustomTextField(
    label:String,
    value:String,
    onChange:(String)->Unit,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingRes: DrawableResource? = null,
    onTrailingIconClick: () -> Unit = {},
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    isDarkMode:Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onChange,
        label = {
            Text(
                text = label,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedLabelColor = Color(0xffF33358),
            unfocusedLabelColor = Color(0xff667085),
            focusedBorderColor = Color(0xffF33358),
            unfocusedBorderColor = Color(if (isDarkMode) 0xff24282B else 0xffD0D5DD),
            cursorColor = Color(0xffF33358),
            focusedTextColor = Color(0xff101828),
            unfocusedTextColor = Color(0xff101828),
            selectionColors = TextSelectionColors(
                handleColor = Color(0xffF33358),
                backgroundColor = Color(0xffF33358).copy(alpha = 0.3f)
            )
        ),
        textStyle = TextStyle(
            color = if(isDarkMode) Color.White else Color.Black,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            fontFamily = PoppinsFontFamily(),
            fontSize = 16.sp
        ),
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = capitalization
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (trailingRes != null){
                Image(vectorResource(trailingRes),null, modifier = Modifier.noRippleClickable(onTrailingIconClick))
            }
        }
    )
}