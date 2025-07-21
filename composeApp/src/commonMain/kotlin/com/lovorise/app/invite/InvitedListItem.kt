package com.lovorise.app.invite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_user
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.vectorResource

@Composable
fun InvitedListItem(name:String,imageUrl:String,isDarkMode:Boolean) {

    Row(modifier = Modifier.fillMaxWidth().height(56.dp), verticalAlignment = Alignment.CenterVertically) {

        AsyncImage(
            model = imageUrl,
            modifier = Modifier.size(40.dp).background(DISABLED_LIGHT, CircleShape).clip(CircleShape),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = name,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

    }

}

@Composable
fun NoInvitedItem(text:String,textColor:Color = Color(0xff98A2B3),onClick:()->Unit = {},isDarkMode: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().height(56.dp).noRippleClickable(onClick), verticalAlignment = Alignment.CenterVertically) {

        Box(modifier = Modifier.size(40.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF2F4F7), CircleShape), contentAlignment = Alignment.Center) {
            Icon(
                tint = if(isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                imageVector = vectorResource(Res.drawable.ic_user),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.Center)
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
            color = textColor
        )

    }
}