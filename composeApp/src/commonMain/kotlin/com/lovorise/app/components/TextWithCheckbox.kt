package com.lovorise.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT

@Composable
fun TextWithCheckbox(
    modifier: Modifier = Modifier,
    height:Dp = 28.dp,
    text:String,
    isChecked:Boolean = false,
    hideCheckBox:Boolean = true,
    onClick: ()->Unit,
    horizontalPadding:Dp = 11.5.dp,
    isDarkMode:Boolean = false
) {

    Row(
        modifier = modifier.fillMaxWidth()
            .height(height)
            .padding(horizontal = horizontalPadding)
            .noRippleClickable(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        Text(
            modifier = Modifier.weight(1f),
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
          //  textAlign = TextAlign.Center,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )


        if (!hideCheckBox) {
            Box(modifier = Modifier.size(24.dp).noRippleClickable(onClick), contentAlignment = Alignment.Center) {
                Checkbox(
                    checked = isChecked,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xffF33358),
                        checkmarkColor = if (isDarkMode) BASE_DARK else Color.White,
                        uncheckedColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                    ),
                    onCheckedChange = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }



    }

}