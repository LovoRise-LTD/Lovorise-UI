package com.lovorise.app.settings.presentation.screens.privacy_and_security.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.blocked_people
import coinui.composeapp.generated.resources.devices
import coinui.composeapp.generated.resources.ic_blocked_people
import coinui.composeapp.generated.resources.ic_chevron_right
import coinui.composeapp.generated.resources.ic_chevron_right_light_color
import coinui.composeapp.generated.resources.ic_devices
import coinui.composeapp.generated.resources.ic_profile_verification
import coinui.composeapp.generated.resources.profile_verification
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SecurityItemsSection(onProfileVerification:()-> Unit,onDevices:()-> Unit,onBlockedPeople:()-> Unit){


    Column {
        Spacer(Modifier.height(8.dp))
        SecurityItemsSectionItem(
            onClick = onProfileVerification,
            title = stringResource(Res.string.profile_verification),
            icon = Res.drawable.ic_profile_verification,
            showDivider = true
        )
        Spacer(Modifier.height(8.dp))
        SecurityItemsSectionItem(
            onClick = onDevices,
            title = stringResource(Res.string.devices),
            icon = Res.drawable.ic_devices,
            showDivider = true
        )
        Spacer(Modifier.height(8.dp))
        SecurityItemsSectionItem(
            onClick = onBlockedPeople,
            title = stringResource(Res.string.blocked_people),
            icon = Res.drawable.ic_blocked_people,
            showDivider = false
        )
        Spacer(Modifier.height(8.dp))
    }




}

@Composable
fun SecurityItemsSectionItem(onClick:()-> Unit,title: String,icon: DrawableResource,showDivider: Boolean){
    Row(Modifier.fillMaxWidth().height(32.dp).noRippleClickable(onClick), verticalAlignment = Alignment.Top) {

        Image(
            modifier = Modifier.size(24.dp),
            imageVector = vectorResource(icon),
            contentDescription = null
        )

        Spacer(Modifier.width(10.dp))

        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().height(24.dp).noRippleClickable(onClick),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = title,
                    color = Color(0xff344054),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.weight(1f))

                Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chevron_right_light_color),
                        contentDescription = null,
                        tint = Color(0xff667085)
                    )
                }

            }
            Spacer(Modifier.weight(1f))
            if (showDivider) {
                CustomDivider()
            }

        }
    }

}
