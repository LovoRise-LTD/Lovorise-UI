package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_verified
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ImageAndTextWithActiveStatus(imageUrl:String,text:String,isOnline:Boolean,isVerified:Boolean,onClick:()->Unit = {},isDarkMode:Boolean) {

    Column (Modifier.width(74.dp).noRippleClickable(onClick)){
        Box(modifier = Modifier.height(77.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))){
            AsyncImage(
                modifier = Modifier.fillMaxSize().background(if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT),
                model = imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            if (isOnline) {
                Box(
                    Modifier.fillMaxSize().padding(end = 5.dp, bottom = 5.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(
                        Modifier.size(11.dp).border(1.dp, Color.White, CircleShape)
                            .background(Color(0xff12B76A), CircleShape)
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Box(Modifier.height(20.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxHeight(),verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 14.52.sp,
                    letterSpacing = (-0.3).sp,
                    color = if (isDarkMode) Color.White else Color(0xff344054)
                )
                if (isVerified) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_verified),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }


    }

}