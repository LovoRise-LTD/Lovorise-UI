package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import coinui.composeapp.generated.resources.ic_location
import coinui.composeapp.generated.resources.ic_verified
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DescriptionProfile(modifier: Modifier = Modifier, showDistance:Boolean = true, distance:Int? = null,showNewBadge:Boolean = false,isAgeVisible:Boolean=true,name:String,age:Int,isVerified:Boolean? = false) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        if (showNewBadge){
            Box(Modifier.padding(bottom = 10.dp).height(31.dp).background(Color(0xffF9E9EC), shape = RoundedCornerShape(50)), contentAlignment = Alignment.Center){
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = stringResource(Res.string.new_here),
                    fontSize = 14.sp,
                    color = Color(0xff475467),
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Name, age and  verification
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name & age
            Text(
                text = buildAnnotatedString {
                    append(name)
                    if (isAgeVisible){
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)){
                            append(" $age")
                        }
                    }
                },
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.width(4.dp))

            if (isVerified == true) {
                // Icon Verification
                Image(
                    modifier = Modifier.size(16.67.dp),
                    painter = painterResource(Res.drawable.ic_verified),
                    contentDescription = "",
                )
            }
        }

        if (showDistance && distance != null) {
            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(Res.drawable.ic_location),
                    tint = Color.White,
                    contentDescription = ""
                )

                Text(
                    text = "$distance km ${stringResource(Res.string.away)}",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily()
                )
            }
        }
//        if (showActionButtons){
//            Spacer(modifier = Modifier.height(19.dp))
////            SwipeActionButtons()
//        }
    }
}