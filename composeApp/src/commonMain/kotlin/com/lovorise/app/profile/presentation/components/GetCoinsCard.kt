package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.get_hearts
import coinui.composeapp.generated.resources.ic_arrow
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.lovorise_hearts
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun GetCoinsCard(
    onGetCoinsClick: ()->Unit,
    coins:Int
) {


    Box(
        modifier = Modifier.fillMaxWidth()
            .height(84.dp)
            .background(color = Color(0xffF9E9EC), shape = RoundedCornerShape(16.dp))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.fillMaxWidth().weight(1f)){

                Text(
                    text = stringResource(Res.string.lovorise_hearts),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xff101828),
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                  //  horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        imageVector = vectorResource(Res.drawable.ic_heart_small),
                        contentDescription = "coins",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(7.dp))

                    Text(
                        modifier = Modifier,
                        text = coins.toString(),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xff101828),
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp
                    )


                }

            }

            Row(
                modifier = Modifier

                    .noRippleClickable { onGetCoinsClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.get_hearts),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xff101828),
                    lineHeight = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.width(7.dp))

                Image(
                    imageVector = vectorResource(Res.drawable.ic_arrow),
                    contentDescription = "coins",
                    modifier = Modifier.size(14.dp)
                )




            }

        }
    }
    
}