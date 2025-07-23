package com.lovorise.app.profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    statusVisible: Boolean,
    onClick: () -> Unit = {},
    showIndicator:Boolean = false,
    isDarkMode:Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                onClick()
            }
    ) {

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = if (isDarkMode) Color(0xff737272) else Color(0xffEAECF0)
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            // Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium
                )
                if (showIndicator) Box(Modifier.size(6.dp).background(Color(0xffF33358), CircleShape))
            }

            Spacer(Modifier.height(8.dp))

            // Description
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = description,
                    maxLines = 3,
                    color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff475467),
                    fontSize = 13.sp,
                    lineHeight = 19.5.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (statusVisible) stringResource(Res.string.visible) else stringResource(Res.string.hidden),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff414C5E),
                    fontSize = 13.sp,
                    lineHeight = 19.5.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(Modifier.height(16.dp))
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(),
//            thickness = 1.dp,
//            color = Color(0xffEAECF0)
//        )
    }
}