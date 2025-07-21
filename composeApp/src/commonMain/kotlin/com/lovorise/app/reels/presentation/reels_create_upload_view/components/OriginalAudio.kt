package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_audio
import coinui.composeapp.generated.resources.original_audio
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OriginalAudio() {

    Box(Modifier.height(33.dp).background(Color(0xff181818).copy(alpha = 0.5f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center){
        Row(
            modifier = Modifier.padding(horizontal = 8.5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.5.dp)
        ) {

            Image(
                imageVector = vectorResource(Res.drawable.ic_audio),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = stringResource(Res.string.original_audio),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                color = Color.White
            )

        }
    }

}