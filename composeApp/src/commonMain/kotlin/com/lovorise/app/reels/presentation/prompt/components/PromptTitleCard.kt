package com.lovorise.app.reels.presentation.prompt.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_pen
import coinui.composeapp.generated.resources.ic_tick_gray
import coinui.composeapp.generated.resources.ic_tick_red
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import org.jetbrains.compose.resources.vectorResource

@Composable
fun PromptTitleCard(text:String,isEditModeEnabled:Boolean,onPromptChange:(String)->Unit,enableEditMode:()->Unit) {

    var prompt by remember { mutableStateOf(text) }

    if (!isEditModeEnabled){
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp)){
            Box(Modifier.padding(vertical = 8.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        text = prompt,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                       // lineHeight = 24.sp,
                        color = Color(0xff344054),
//                        maxLines = 1
                    )
                //    Spacer(Modifier.weight(1f))
                    Box(
                        Modifier.size(24.dp).noRippleClickable(enableEditMode),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = vectorResource(Res.drawable.ic_pen),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }else{
        Column {
            DescriptionTextField(
                value = prompt,
                onValueChange = {
                    if (it.length <= 60) {
                        prompt = it
                    }
                },
                label = "",
                height = 68.dp,
                cornerRadiusPercent = 12,
                borderColor = Color(0xff344054),
                cursorColor = Color(0xffF33358),
                textStyle = TextStyle(
                    letterSpacing = 0.2.sp,
                    //  lineHeight = 24.sp,
                    fontSize = 12.sp,
                    color = Color(0xff344054),
                    fontWeight = FontWeight.Medium,
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "${prompt.length}/60",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff475467),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )
                Spacer(Modifier.weight(1f))

                Box(Modifier.size(16.dp).noRippleClickable { if (prompt.isNotBlank()) onPromptChange(prompt) }, contentAlignment = Alignment.Center){
                    Image(
                        imageVector = vectorResource(if (prompt.isNotBlank()) Res.drawable.ic_tick_red else Res.drawable.ic_tick_gray),
                        contentDescription = null,
                     //   modifier = Modifier.height(7.5.dp).width(10.5.dp)
                    )
                }

            }
        }
    }


}