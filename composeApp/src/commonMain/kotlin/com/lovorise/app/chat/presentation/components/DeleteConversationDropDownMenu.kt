package com.lovorise.app.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteConversationDropDownMenu(
    expanded:Boolean,
    onDelete:()->Unit,
    onDismissRequest:()->Unit,
) {

    DropdownMenu(
        modifier = Modifier
            .width(184.dp),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        properties = PopupProperties()
    ) {
        Column(
            modifier = Modifier
                .width(184.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Box(
//                Modifier.height(0.5.dp).fillMaxWidth()
//                    .background(Color(0xff98A2B3).copy(alpha = 0.42f))
//            )

            Box(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 12.dp)
                    .noRippleClickable(onDelete),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = stringResource(Res.string.delete_conversation),
                    fontSize = 14.sp,
                    color = Color(0xff101828),
                    lineHeight = 21.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal
                )
            }


        }
    }
}