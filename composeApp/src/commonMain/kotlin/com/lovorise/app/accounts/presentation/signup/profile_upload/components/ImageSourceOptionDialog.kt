package com.lovorise.app.accounts.presentation.signup.profile_upload.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.change_profile
import coinui.composeapp.generated.resources.delete_profile
import coinui.composeapp.generated.resources.take_a_photo
import coinui.composeapp.generated.resources.upload_a_photo_or_video
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import org.jetbrains.compose.resources.stringResource

@Composable
fun ImageSourceOptionDialog(
    onGalleryRequest: () -> Unit = {},
    onCameraRequest: () -> Unit = {},
    onDeleteRequest: () -> Unit = {},
    isDarkMode: Boolean,
    showDeleteOption:Boolean,
    isUpdate:Boolean,
) {
    Column(
        Modifier
            .background(if(isDarkMode) BASE_DARK else Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 10.dp
                )
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Box(Modifier.fillMaxWidth().height(16.dp), contentAlignment = Alignment.Center) {
                Box(Modifier.width(40.dp).height(2.dp).background(Color(0xff667085), shape = RoundedCornerShape(50)))
            }


            if (showDeleteOption) {
                Box(
                    Modifier.fillMaxWidth().height(44.dp).padding(start = 10.dp).noRippleClickable(onDeleteRequest),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(Res.string.delete_profile),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(0xffD92D20),
                        letterSpacing = 0.2.sp
                    )

                }
            }





            Box(Modifier.fillMaxWidth().height(44.dp).padding(start = 10.dp).noRippleClickable(onGalleryRequest), contentAlignment = Alignment.CenterStart){
                Text(
                    text = if (isUpdate) stringResource(Res.string.change_profile) else stringResource(Res.string.upload_a_photo_or_video),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = if(isDarkMode) Color.White else Color(0xff101828),
                    letterSpacing = 0.2.sp
                )
            }

            Box(Modifier.fillMaxWidth().height(44.dp).padding(start = 10.dp).noRippleClickable(onCameraRequest), contentAlignment = Alignment.CenterStart){
                Text(
                    text = stringResource(Res.string.take_a_photo),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = if(isDarkMode) Color.White else Color(0xff101828),
                    letterSpacing = 0.2.sp
                )
            }




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }
}