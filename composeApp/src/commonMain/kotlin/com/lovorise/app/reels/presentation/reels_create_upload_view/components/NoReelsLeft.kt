package com.lovorise.app.reels.presentation.reels_create_upload_view.components

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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.change_my_filters
import coinui.composeapp.generated.resources.create_reel
import coinui.composeapp.generated.resources.expand_your_experience_by_changing_filter_or_check_later
import coinui.composeapp.generated.resources.ic_earth
import coinui.composeapp.generated.resources.you_have_watched_all_the_reels
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NoReelsLeft(
    onCreateReelAction:()->Unit,
    isDarkMode:Boolean,
   // onUnlockMoreReels:()->Unit,
    onChangeFilter:()->Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 35.dp)
            //.background(Color.White)
            .fillMaxWidth(),
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
            modifier = Modifier.padding(horizontal = 64.5.dp),
            text = stringResource(Res.string.you_have_watched_all_the_reels),
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
            modifier = Modifier.padding(horizontal = 50.dp),
            text = stringResource(Res.string.expand_your_experience_by_changing_filter_or_check_later),
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
                .noRippleClickable(onChangeFilter),
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

        Text(
            modifier = Modifier.noRippleClickable(onCreateReelAction),
            text = stringResource(Res.string.create_reel).replace('R','r'),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp,
            //  lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = if (isDarkMode) Color.White else Color(0xff344054)
        )

    }

}