package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatInboxMoreOptionsBottomSheetContent(
    isDarkMode:Boolean,
    onUnmatch:()->Unit,
    onBlock:()->Unit,
    onReport:()->Unit
) {

    Column(
        modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxWidth()

    ) {

        Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .height(2.dp)
                    .width(40.dp)
                    .background(Color(0xff667085))
            )
        }

        Spacer(Modifier.height(8.dp))

        OptionItem(
            onClick = onUnmatch,
            title = stringResource(Res.string.unmatch),
            isDarkMode = isDarkMode
        )


        Spacer(Modifier.height(8.dp))
        OptionItem(
            onClick = onBlock,
            title = stringResource(Res.string.block),
            isDarkMode = isDarkMode
        )


        Spacer(Modifier.height(8.dp))

        OptionItem(
            onClick = onReport,
            title = stringResource(Res.string.report),
            isDarkMode = isDarkMode
        )


        Spacer(Modifier.height(8.dp))



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }

}

@Composable
fun OptionItem(onClick:()->Unit,title:String,isDarkMode: Boolean) {
    Box(
        modifier = Modifier.height(40.dp).fillMaxWidth().padding(horizontal = 16.dp)
            .noRippleClickable(onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828),
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )
    }
}