package com.lovorise.app.lovorise_hearts.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.hearts
import coinui.composeapp.generated.resources.ic_heart_small
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BuyLovoriseHeartsItems(item: BuyLovoriseHeartsItem, onClick : () ->Unit,isDarkMode:Boolean) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .background(if (isDarkMode) CARD_BG_DARK else Color(0xffF4F6FA), RoundedCornerShape(16.dp))
            //.border(BorderStroke(1.dp, if (isDarkMode) CARD_BG_DARK else Color(0xffD0D5DD).copy(alpha = 0.3f)), RoundedCornerShape(16.dp))
    ){

        Row(
            modifier = Modifier
                .fillMaxSize()
               // .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_heart_small),
                    contentDescription = "hearts",
                    modifier = Modifier.size(24.dp)
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.coins,
                    fontWeight = FontWeight.SemiBold, fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    //   lineHeight = 26.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.hearts),
                    fontWeight = FontWeight.Normal, fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) Color.White else  Color(0xff101828)
                )

            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .height(31.dp)
                    .width(73.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xffF3335D), Color(0xffF33386))
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    .noRippleClickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = item.amount,
                    textAlign = TextAlign.Center,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White,
                  //  lineHeight = 16.52.sp,
                    letterSpacing = 0.14.sp
                )


            }




        }

    }


    
}

data class BuyLovoriseHeartsItem (
   // val bonus:Int,
    val coins:String,
    val amount:String
)