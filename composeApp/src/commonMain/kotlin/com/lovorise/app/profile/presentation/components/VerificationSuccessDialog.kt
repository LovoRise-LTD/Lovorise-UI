package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import coinui.composeapp.generated.resources.ic_verified
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.verification_success_message
import coinui.composeapp.generated.resources.verification_successful
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun VerificationSuccessDialog(onCancel:()->Unit,isDarkMode:Boolean) {

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

                Spacer(Modifier.height(60.dp))
                Image(
                    imageVector = vectorResource(Res.drawable.ic_verified),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.verification_successful),
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.verification_success_message),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp
                )



                Spacer(Modifier.height(16.dp))

                ButtonWithText(
                    text = stringResource(Res.string.ok),
                    bgColor = Color(0xffF33358),
                    textColor = Color(0xffFFFFFF),
                    onClick = onCancel
                )
                Spacer(Modifier.height(29.dp))



            }







        }



    }

}
