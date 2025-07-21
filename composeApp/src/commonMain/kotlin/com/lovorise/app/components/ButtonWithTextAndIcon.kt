package com.lovorise.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ButtonWithTextAndIcon(
    modifier: Modifier = Modifier,
    bgColor:Color = Color(0XFFF33358),
    icon:DrawableResource,
    cornerRadiusPercent:Int = 40,
    text:String
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(bgColor, RoundedCornerShape(cornerRadiusPercent)),
        contentAlignment = Alignment.Center
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier.size(16.dp),
                imageVector = vectorResource(icon),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(13.dp))

            Text(
                text = text,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp
            )


        }


    }




}