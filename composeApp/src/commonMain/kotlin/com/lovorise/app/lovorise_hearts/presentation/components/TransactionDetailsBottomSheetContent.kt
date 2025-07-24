package com.lovorise.app.lovorise_hearts.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.date_and_time
import coinui.composeapp.generated.resources.hearts
import coinui.composeapp.generated.resources.ic_dismiss_popup
import coinui.composeapp.generated.resources.ic_dismiss_transaction_item
import coinui.composeapp.generated.resources.ic_heart
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.ok
import coinui.composeapp.generated.resources.you_are_in_spotlight
import coinui.composeapp.generated.resources.you_are_in_spotlight_message
import coinui.composeapp.generated.resources.you_are_in_super_spotlight
import coinui.composeapp.generated.resources.you_are_in_super_spotlight_message
import coinui.composeapp.generated.resources.your_profile_is_getting_extra_visibility_for_the_next_hours
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun TransactionDetailsBottomSheetContent(isDarkMode:Boolean,onCancel:()->Unit,transactionData: TransactionData) {


    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
           // verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row (Modifier.height(24.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.weight(1f))

                Image(
                    modifier = Modifier.noRippleClickable(onCancel),
                    painter = painterResource(Res.drawable.ic_xmark),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = transactionData.name,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                color =if (isDarkMode) Color.White else  Color(0xff475467)
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(34.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Image(
                    imageVector = vectorResource(Res.drawable.ic_heart_small),
                    contentDescription = "coins",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))

                Text(
                    modifier = Modifier,
                    text = transactionData.change.toString(),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )


            }

            Spacer(Modifier.height(8.dp))

            Text(
//                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                text = stringResource(Res.string.hearts),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.sp,
                lineHeight = 20.sp,
//                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054)
            )

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                text = transactionData.change.toString(),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                lineHeight = 24.sp,
//                textAlign = TextAlign.Center,
                color =if (isDarkMode) DISABLED_LIGHT else  Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))

            CustomDivider()

            Spacer(Modifier.height(16.dp))


            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                text = stringResource(Res.string.date_and_time),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.sp,
                lineHeight = 20.sp,
//                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054)
            )

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                text = transactionData.formattedDate,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                lineHeight = 24.sp,
//                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff101828)
            )
            Spacer(Modifier.height(24.dp))

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }
}