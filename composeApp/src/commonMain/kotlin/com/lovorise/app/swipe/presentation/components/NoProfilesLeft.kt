package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NoProfilesLeft(onClick:()->Unit,isDarkMode:Boolean) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            //.background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            imageVector = vectorResource(Res.drawable.ic_earth),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )


        Spacer(Modifier.height(24.dp))


        Text(
            modifier = Modifier.padding(horizontal = 60.dp),
            text = stringResource(Res.string.you_have_swiped_everyone_nearby),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 48.dp),
            text = stringResource(Res.string.change_filter_or_check_later),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = if (isDarkMode) Color.White else Color(0xff344054)
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                // .fillMaxWidth(0.45f)
                .height(40.dp)
                .background(Color(0xffF33358), shape = RoundedCornerShape(16.dp))
                .noRippleClickable(onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = stringResource(Res.string.change_my_filters),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                //  lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xffffffff)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}