package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.visible_on_profile
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun CheckboxAndText(
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    text:String = stringResource(Res.string.visible_on_profile),
    isChecked:Boolean = false,
    hideCheckBox:Boolean = false,
    onClick: ()->Unit,
    spacing:Dp = 10.dp,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
    isDarkMode:Boolean = false
) {

    Row(
        modifier = modifier.fillMaxWidth().noRippleClickable(onClick),
        verticalAlignment = alignment,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ){

        if (!hideCheckBox) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Checkbox(
                    checked = isChecked,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xffF33358),
                        checkmarkColor = if (isDarkMode) CARD_BG_DARK else Color.White,
                        uncheckedColor = Color(0xff98A2B3)
                    ),
                    onCheckedChange = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

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

    }

}