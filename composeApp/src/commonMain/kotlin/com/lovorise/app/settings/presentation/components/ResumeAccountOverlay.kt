package com.lovorise.app.settings.presentation.components

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
import coinui.composeapp.generated.resources.ic_resume_account
import coinui.composeapp.generated.resources.resume_account_msg
import coinui.composeapp.generated.resources.resume_my_account
import coinui.composeapp.generated.resources.resume_my_account_caps
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ResumeAccountOverlay(onResume: () -> Unit,isLoading:Boolean) {

    Box(Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(top = 100.dp).noRippleClickable{}, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
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
                color = Color.White,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.resume_account_msg),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color.White,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                    .noRippleClickable(onResume),
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

        if (isLoading){
            CircularLoader()
        }
    }

}