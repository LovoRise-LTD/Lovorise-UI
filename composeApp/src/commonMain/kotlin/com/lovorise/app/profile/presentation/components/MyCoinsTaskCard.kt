package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coinui.composeapp.generated.resources.ic_heart_small
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.CustomLinearProgressIndicator
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.domain.models.GetCoinTask
import com.lovorise.app.profile.domain.models.IndividualTask
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MyCoinsTaskCard(modifier: Modifier = Modifier,task: GetCoinTask,isDarkMode:Boolean) {

    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0XFFF33358)
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                modifier = Modifier,
                text = task.taskTitle,
                fontFamily = PoppinsFontFamily(),
                color = Color(0XFFF33358),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(8.dp))

            task.individualTasks.forEachIndexed { index, individualTask ->


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.50f),
                        text = individualTask.task,
                        fontFamily = PoppinsFontFamily(),
                        color = if (isDarkMode) Color.White else Color(0XFF101828),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        letterSpacing = 0.2.sp

                    )

                    Spacer(Modifier.weight(1f))



                    Text(
                        modifier = Modifier,
                        text = individualTask.coins.toString(),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = if (isDarkMode) Color.White else Color(0XFF101828),
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(Modifier.width(10.dp))

                    Box(modifier = Modifier.height(23.dp).width(18.dp), contentAlignment = Alignment.Center) {
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_heart_small),
                            contentDescription = "coins",
                            modifier = Modifier.size(18.dp)
                        )
                    }

              //      Spacer(Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .height(23.dp)
                            .width(75.dp)
                            .padding(start = 10.dp)
                            .background(if(individualTask.buttonInfo.isEnabled) IndividualTask.EnabledBtnColor else{ if (isDarkMode) IndividualTask.DisabledDarkBtnColor else IndividualTask.DisabledBtnColor}, shape = RoundedCornerShape(50))
                            .noRippleClickable { if (individualTask.buttonInfo.isEnabled){ individualTask.buttonInfo.onClick()} },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = individualTask.buttonInfo.text,
                            textAlign = TextAlign.Center,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            color = if (individualTask.buttonInfo.isEnabled) IndividualTask.EnabledBtnTxtColor else { if (isDarkMode) IndividualTask.DisabledDarkTxtBtnColor else IndividualTask.DisabledTxtBtnColor },
                            lineHeight = 16.52.sp,
                            letterSpacing = 0.14.sp
                        )
                    }


                }


                individualTask.taskProgress?.let {

                    Spacer(Modifier.height(7.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

//                        Box(
//                            modifier = Modifier.fillMaxWidth().height(5.dp).padding(end = 75.dp),
//
//                        )

//                        LinearProgressIndicator(
//                            modifier = Modifier.fillMaxWidth().height(5.dp).padding(end = 75.dp),
//                            trackColor = Color(0XFFEAECF0),
//                            strokeCap = StrokeCap.Butt,
//                            color = Color(0XFFF33358),
//                            progress = { it.completed.toFloat() / it.total.toFloat() },
//                            gapSize = 0.dp
//                        )

                        CustomLinearProgressIndicator(
                            progress = it.completed.toFloat() / it.total.toFloat(),
                            isDarkMode = isDarkMode,
                            modifier = Modifier.fillMaxWidth().height(5.dp).padding(end = 75.dp),
                            strokeWidth = 5.dp
                        )

                        Text(
                            text = "${it.completed}/${it.total}",
                            fontFamily = PoppinsFontFamily(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0XFF98A2B3),
                            lineHeight = 14.sp,
                            letterSpacing = 0.2.sp
                        )
                    }


                }

                if (index != task.individualTasks.lastIndex) {
                    Spacer(Modifier.height(12.dp))
                }


            }


        }
    }

}