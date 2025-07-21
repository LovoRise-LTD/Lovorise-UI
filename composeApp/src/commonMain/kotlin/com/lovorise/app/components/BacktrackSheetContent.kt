package com.lovorise.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import coinui.composeapp.generated.resources.ic_coin
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BacktrackSheetContent(
    onCancelClick:()->Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(Res.string.backtrack),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp
        )

        Text(
            text = stringResource(Res.string.backtrack_to_last_swipe),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 19.5.sp,
            letterSpacing = 0.2.sp,
            color = Color(0xff344054)
        )



        Box(
            modifier = Modifier
                .width(201.dp)
                .height(40.dp)
                .background(Color(0XFFF33358), RoundedCornerShape(45)),
            contentAlignment = Alignment.Center
        ){

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.use_coins),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.width(5.dp))

                Box(modifier = Modifier.size(21.dp), contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_coin),
                        contentDescription = "coins",
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                Text(
                    text = "50",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.White,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp
                )


            }



        }


        Text(
            text = stringResource(Res.string.cancel),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xff98A2B3),
            lineHeight = 24.sp,
            letterSpacing = 0.2.sp,
            modifier = Modifier.noRippleClickable { onCancelClick() }
        )

    }
}