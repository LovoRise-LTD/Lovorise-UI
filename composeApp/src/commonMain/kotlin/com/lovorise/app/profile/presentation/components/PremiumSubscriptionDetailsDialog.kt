package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.cancel_subscription
import coinui.composeapp.generated.resources.monthly
import coinui.composeapp.generated.resources.next_renewal_date
import coinui.composeapp.generated.resources.pay_plan
import coinui.composeapp.generated.resources.status
import coinui.composeapp.generated.resources.subscribed
import coinui.composeapp.generated.resources.weekly
import coinui.composeapp.generated.resources.your_lovorise_premium_monthly
import coinui.composeapp.generated.resources.your_lovorise_premium_weekly
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.SubscriptionType
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@Composable
fun PremiumSubscriptionDetailsDialog(
    onPositive:()->Unit,
    onCancel:()->Unit,
    type: SubscriptionType,
    expiresDate:String,
    isDarkMode:Boolean
) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .background(if (isDarkMode) BASE_DARK else Color.White, shape = RoundedCornerShape(16.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 17.dp)
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                    text = stringResource(if(type == SubscriptionType.WEEKLY) Res.string.your_lovorise_premium_weekly else Res.string.your_lovorise_premium_monthly),
                    fontSize = 18.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    lineHeight = 27.sp,
                    letterSpacing = (-0.3).sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.status),
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )

                    Text(
                        text = stringResource(Res.string.subscribed),
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(Modifier.height(9.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.next_renewal_date),
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )

                    Text(
                        text = expiresDate,
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(Modifier.height(9.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.pay_plan),
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )

                    Text(
                        text = if (type == SubscriptionType.WEEKLY) "$ 1.99 / ${stringResource(Res.string.weekly).lowercase()}" else "$ 3.99 / ${stringResource(Res.string.monthly).lowercase()}",
                        fontSize = 14.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        lineHeight = 21.sp,
                        letterSpacing = (0.2).sp,
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(Modifier.height(10.dp))


                Text(
                    modifier = Modifier.noRippleClickable(onPositive),
                    text = stringResource(Res.string.cancel_subscription),
                    fontSize = 14.sp,
                    color = Color(0xffF33358),
                    lineHeight = 21.sp,
                    letterSpacing = (0.2).sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                )


            }

        }

    }

}