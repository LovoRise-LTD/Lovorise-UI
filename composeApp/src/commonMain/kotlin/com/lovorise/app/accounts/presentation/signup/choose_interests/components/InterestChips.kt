package com.lovorise.app.accounts.presentation.signup.choose_interests.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK

@Composable
fun InterestChips(
    text:String,
    isSelected:Boolean,
    onClick:()->Unit,
    isDarkMode:Boolean
) {


    Box(
        modifier = Modifier
            .height(33.dp)
            .then(
                if (isSelected) Modifier
                    .background(Color(0xffF33358), shape = RoundedCornerShape(50))
                else Modifier
                    .border(width = 1.dp, if (isDarkMode) CARD_BG_DARK else Color(0xffD0D5DD).copy(alpha = 0.3f), shape = RoundedCornerShape(50))
            )
            .noRippleClickable(onClick),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
            color = if (!isSelected) Color(0xff667085) else Color.White
        )

    }
}