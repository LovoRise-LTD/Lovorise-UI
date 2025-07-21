package com.lovorise.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_search
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.ui.CARD_BG_DARK
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SearchTextField(
    label:String,
    query:String,
    onQueryChange:(String)->Unit,
    roundedCornerPercent:Int = 40,
    cursorColor:Brush = SolidColor(Color.Black),
    isDarkMode:Boolean = false,
    addBgInDarkMode:Boolean = true
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .then(if (isDarkMode && addBgInDarkMode) Modifier.background(CARD_BG_DARK, shape = RoundedCornerShape(roundedCornerPercent)) else Modifier.border(width = 1.dp,Color(0xFFEAECF0), shape = RoundedCornerShape(roundedCornerPercent)))

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = vectorResource(Res.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier.size(20.dp)
            )
            BasicTextField(
                cursorBrush = cursorColor,
                value = query,
                onValueChange = onQueryChange,
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth().weight(1f),
                decorationBox = { padding ->
                    if (query.isBlank()) {
                        Text(
                            text = label,
                            color = Color(0xff98A2B3),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.2.sp,
                            lineHeight = 24.sp,
                            fontFamily = PoppinsFontFamily()
                        )
                    }
                    padding()
//                    innerTextField() // The actual text field
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.2.sp,
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if(isDarkMode) Color.White else Color.Black
                )
            )
        }
    }
}
