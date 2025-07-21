package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import coinui.composeapp.generated.resources.ic_delete_message
import coinui.composeapp.generated.resources.ic_message_reply
import coinui.composeapp.generated.resources.reply
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MessageReplyButton(onClick:()->Unit,onDelete:()->Unit,isDarkMode:Boolean) {

    Column {
        if (!isDarkMode){
            CustomDivider()
        }
        Row(
            Modifier.fillMaxWidth().height(56.dp).background(if (isDarkMode) CARD_BG_DARK else Color.White).padding(horizontal = 16.dp).noRippleClickable(onClick),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = vectorResource(Res.drawable.ic_message_reply),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = stringResource(Res.string.reply),
                fontWeight = FontWeight.Medium,
                fontFamily = PoppinsFontFamily(),
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = vectorResource(Res.drawable.ic_delete_message),
                contentDescription = null,
                modifier = Modifier.size(24.dp).noRippleClickable(onDelete),
                tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

        }
    }
}