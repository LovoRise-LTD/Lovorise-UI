package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LikesComponent(imageUrl:String,isBlurred:Boolean,likes:Int,onClick:()->Unit,isDarkMode:Boolean) {

    Column (Modifier.width(74.dp)){

        Box(modifier = Modifier.height(77.dp).clip(RoundedCornerShape(8.dp)).fillMaxWidth().noRippleClickable(onClick)){
            AsyncImage(
                modifier = Modifier.fillMaxSize().then(if (isBlurred) Modifier.blur(10.dp) else Modifier).then(Modifier.background(if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT)),
                model = imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )


            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (likes > 0) {
                    Box(
                        modifier = Modifier.size(24.49.dp)
                            .border(width = 0.82.dp, color = Color.White, CircleShape)
                            .background(Color(0xffF33358), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (likes > 99) "99+" else "$likes",
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 9.18.sp,
                            lineHeight = 11.11.sp,
                            letterSpacing = (-0.23).sp,
                            color = Color.White
                        )
                    }
                } else {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_rate_app_logo),
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )

                }
            }



        }

        Spacer(Modifier.height(6.dp))

        Box(Modifier.height(20.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(Res.string.likes),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 14.52.sp,
                letterSpacing = (-0.3).sp,
                color = if (isDarkMode) Color.White else Color(0xff344054)
            )
        }

    }

}