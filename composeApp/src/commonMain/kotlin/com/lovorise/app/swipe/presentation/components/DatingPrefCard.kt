package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.i_m_looking_for
import coinui.composeapp.generated.resources.ic_heart_arrow
import coinui.composeapp.generated.resources.ic_looking_for
import com.lovorise.app.PoppinsFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun LookingForSection(infos:List<DatingPrefCardInfo>,isDarkMode:Boolean){

    if (infos.isNotEmpty()) {
        Box(
            Modifier.fillMaxWidth().border(
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = Color(0xffEAECF0)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth().height(21.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp),
                        imageVector = vectorResource(Res.drawable.ic_looking_for),
                        contentDescription = "",
                        tint = if (isDarkMode) Color.White else Color(0xff101828)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = stringResource(Res.string.i_m_looking_for),
                        fontSize = 14.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        lineHeight = 20.sp,
                        letterSpacing = 0.2.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium
                    )

                }

                Spacer(Modifier.height(10.dp))

                infos.forEach {

                    DatingPrefCard(it)
                    if (infos.lastOrNull() != it) {
                        Spacer(Modifier.height(10.dp))
                    }
                }

                Spacer(Modifier.height(11.dp))
            }
        }
    }


}



@Composable
fun DatingPrefCard(info: DatingPrefCardInfo) {

    Box(
        modifier = Modifier
            //.fillMaxWidth()
            .height(42.dp)
            .background(color = info.bgColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
              //  .fillMaxWidth()
                .height(24.dp)
                .padding(start = 10.dp, end = info.endPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                imageVector = vectorResource(info.drawableResource),
                contentDescription = "",
            )

            Text(
                text = info.text,
                fontSize = 14.sp,
                color = info.txtColor,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold
            )




        }
    }

}


data class DatingPrefCardInfo(
    val bgColor:Color = Color(0xffF9E9EC),
    val txtColor:Color = Color(0xffD20F34),
    val drawableResource: DrawableResource = Res.drawable.ic_heart_arrow,
    val text:String = "Life partner",
    val endPadding: Dp
)