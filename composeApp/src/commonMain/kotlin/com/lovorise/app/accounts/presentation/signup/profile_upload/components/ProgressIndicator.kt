package com.lovorise.app.accounts.presentation.signup.profile_upload.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.saving
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.CustomLinearProgressIndicator
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProgressIndicator(progress:Float) {
    val animatedProgress by animateFloatAsState(targetValue = progress)

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp).padding(bottom = 14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(Res.string.saving),
            color = Color(0xff344054),
            textAlign = TextAlign.Center,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            lineHeight = 24.34.sp,
            fontSize = 16.22.sp,
            letterSpacing = 0.12.sp
        )

        Spacer(Modifier.height(13.91.dp))

        CustomLinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            strokeWidth = 9.27.dp,
            progress = animatedProgress,
            color = Color(0xffF33358),
            backgroundColor = Color(0xffD9D9D9)
        )
    }


}