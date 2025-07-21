package com.lovorise.app.reels.presentation.prompt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.prompt_tabs
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun PromptTabs(
    activeIndex:Int,
    onIndexChange:(Int)->Unit
) {

    val tabs = stringArrayResource(Res.array.prompt_tabs)

    @Composable
    fun PromptTab(modifier: Modifier = Modifier, text:String, bgColor: Color, textColor: Color, onClick:()->Unit) {
        Box(modifier.height(44.dp).noRippleClickable(onClick).background(color = bgColor, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center){
            Text(
                text = text,
                color = textColor,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily(),
                fontSize = 16.sp
            )

        }
    }

    Column(Modifier.padding(vertical = 16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

            tabs.forEachIndexed {i,item->
                PromptTab(
                    text = item,
                    onClick = {onIndexChange(i)},
                    textColor = Color(if (i == activeIndex) 0xffFFFFFF else 0xff344054),
                    bgColor = Color(if (i == activeIndex) 0xffF33358 else 0xffEAECF0),
                    modifier = Modifier.weight(1f)
                )
                if (i == 0 || i == 1){
                    Spacer(Modifier.width(16.dp))
                }

            }


        }
    }




}