package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.get_the_verified_badge
import coinui.composeapp.generated.resources.get_verified_badge_message
import coinui.composeapp.generated.resources.ic_scan_document
import coinui.composeapp.generated.resources.ic_verified
import coinui.composeapp.generated.resources.not_right_now
import coinui.composeapp.generated.resources.photos
import coinui.composeapp.generated.resources.photos_plus_id
import coinui.composeapp.generated.resources.tap_to_verify
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.profile_upload.components.dashedBorder
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun GetVerifiedBadgeDialog(onCancel:()->Unit,onPhotoVerification:()->Unit,onIDPhotoVerification:()->Unit,isDarkMode:Boolean) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(if (isDarkMode) CARD_BG_DARK else Color.White, shape = RoundedCornerShape(16.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                // .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
                //  horizontalAlignment = Alignment.CenterStart
            ) {

                Spacer(Modifier.height(19.dp))
                Image(
                    imageVector = vectorResource(Res.drawable.ic_verified),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(Res.string.get_the_verified_badge),
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(Res.string.get_verified_badge_message),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    VerifyButton(
//                        modifier = Modifier.weight(1f).aspectRatio(159 / 135f),
//                        title = stringResource(Res.string.photos),
//                        onClick = onPhotoVerification,
//                        isDarkMode = isDarkMode
//                    )
                    VerifyButton(
                        modifier = Modifier.fillMaxWidth().height(116.dp),
                        title = stringResource(Res.string.photos_plus_id),
                        onClick = onIDPhotoVerification,
                        isDarkMode = isDarkMode
                    )

                }

                Spacer(Modifier.height(16.dp))

                ButtonWithText(
                    text = stringResource(Res.string.not_right_now),
                    bgColor = if (isDarkMode) Color(0xff737272) else Color(0xffEAECF0),
                    textColor = if (isDarkMode) Color(0xffBEC1C6) else Color(0xff344054),
                    onClick = onCancel
                )
                Spacer(Modifier.height(19.dp))



            }







        }



    }

}

@Composable
fun VerifyButton(title:String,onClick:()->Unit,modifier: Modifier=Modifier,isDarkMode: Boolean) {
    Box(modifier.fillMaxWidth().dashedBorder(1.dp,16.dp, Color(0xffD0D5DD)).noRippleClickable(onClick)) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = vectorResource(Res.drawable.ic_scan_document),
                contentDescription = null
            )

           Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.18.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(Res.string.tap_to_verify),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.18.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085)
            )
        }

    }

}