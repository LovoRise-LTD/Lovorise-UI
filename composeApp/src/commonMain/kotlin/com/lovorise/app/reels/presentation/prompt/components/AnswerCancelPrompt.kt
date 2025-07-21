package com.lovorise.app.reels.presentation.prompt.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.answer_prompt
import coinui.composeapp.generated.resources.ic_tick_gray
import coinui.composeapp.generated.resources.ic_tick_red
import coinui.composeapp.generated.resources.ic_xmark
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AnswerCancelPrompt(onBack:()->Unit,onSave:()->Unit,isSaveButtonEnabled:Boolean) {

    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            Modifier.size(24.dp).noRippleClickable(onBack),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = vectorResource(Res.drawable.ic_xmark),
                contentDescription = null,
                modifier = Modifier.size(13.9.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(Res.string.answer_prompt),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp
        )

        Spacer(Modifier.weight(1f))

        Box(
            Modifier.size(24.dp).noRippleClickable(onSave),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = vectorResource(if (isSaveButtonEnabled) Res.drawable.ic_tick_red else Res.drawable.ic_tick_gray),
                contentDescription = null,
              //  modifier = Modifier.height(11.25.dp).width(15.75.dp)
            )
        }

    }
}