package com.lovorise.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import coinui.composeapp.generated.resources.ic_left
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HeaderWithTitleAndBack(onBack:()->Unit,title:String,addShadow:Boolean = true,backIcon:DrawableResource = Res.drawable.ic_left,isDarkMode:Boolean = false) {

    Row(
        modifier = Modifier.fillMaxWidth().height(55.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier.fillMaxHeight().size(24.dp)
                    .noRippleClickable(onBack),
                contentAlignment = Alignment.CenterStart
            ) {
                if (isDarkMode) {
                    Icon(
                        modifier = Modifier.width(18.dp).height(14.dp),
                        imageVector = vectorResource(backIcon),
                        contentDescription = "back",
                        tint = Color.White
                    )
                }else{
                    Image(
                        modifier = Modifier.width(18.dp).height(14.dp),
                        imageVector = vectorResource(backIcon),
                        contentDescription = "back"
                    )
                }
            }



            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = title,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )
            }


        }


    }
    if (addShadow) {
        DropShadow()
    }

}