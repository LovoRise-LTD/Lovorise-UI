package com.lovorise.app.reels.presentation.reels_create_upload_view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.create_reel
import coinui.composeapp.generated.resources.create_reel_message
import coinui.composeapp.generated.resources.ic_camera_black
import coinui.composeapp.generated.resources.ic_create_reels
import coinui.composeapp.generated.resources.ic_resume_account
import coinui.composeapp.generated.resources.resume_account_msg
import coinui.composeapp.generated.resources.resume_my_account
import coinui.composeapp.generated.resources.resume_my_account_caps
import coinui.composeapp.generated.resources.your_personalized_feed_on_way
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DefaultCreateReelsScreen(onCreateButtonClick:()->Unit,showAccountResume:Boolean,onResumeAccount:()->Unit,isLoading:Boolean) {

    if (!showAccountResume) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Box(
                Modifier.size(58.dp)
                    .background(
                        Color(0xffFF7791).copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    imageVector = vectorResource(Res.drawable.ic_create_reels),
                    contentDescription = null,
                    modifier = Modifier.size(26.1.dp)
                )

            }

            Spacer(Modifier.height(16.dp))

            Box(
                Modifier.fillMaxWidth().height(39.dp).padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.your_personalized_feed_on_way),
                    color = Color(0xff101828),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.2.sp,
                    lineHeight = 19.5.sp,
                    fontFamily = PoppinsFontFamily()
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.create_reel_message),
                color = Color(0xff344054),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 49.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily()
            )
            Spacer(Modifier.height(16.dp))
            ButtonWithText(
                modifier = Modifier.fillMaxWidth(0.7f),
                text = stringResource(Res.string.create_reel),
                bgColor = Color(0xffF33358),
                textColor = Color.White,
                onClick = onCreateButtonClick
            )


        }
    }

    if (showAccountResume) {
        Box(
            Modifier.fillMaxSize().padding(horizontal = 19.dp).noRippleClickable {},
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Box(
                    Modifier.size(35.dp).background(Color(0xffF33358), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_resume_account),
                        contentDescription = null,
                        modifier = Modifier.width(14.dp).height(12.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.resume_my_account_caps),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    color = Color(0xff101828),
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.resume_account_msg),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = Color(0xff344054),
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                        .noRippleClickable(onResumeAccount),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 31.dp),
                        text = stringResource(Res.string.resume_my_account),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

            }

            Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
                Box(
                    Modifier.padding(top = 14.dp).size(24.dp)
                        .noRippleClickable(onCreateButtonClick),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_camera_black),
                        contentDescription = null,
                        modifier = Modifier.size(height = 18.dp, width = 22.dp)
                    )
                }
            }

            if (isLoading) {
                CircularLoader()
            }
        }

    }


}