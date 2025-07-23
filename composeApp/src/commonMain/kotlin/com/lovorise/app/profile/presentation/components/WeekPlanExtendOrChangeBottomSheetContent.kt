package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.enjoy_uninterrupted_access_with_a_better_value
import coinui.composeapp.generated.resources.extend_for_one_more_week
import coinui.composeapp.generated.resources.not_now
import coinui.composeapp.generated.resources.stay_premium_for_seven_more_days_one_time
import coinui.composeapp.generated.resources.switch_to_lovorise_monthly_plan
import coinui.composeapp.generated.resources.your_current_plan_ends_tomorrow_dont_lose_premium_features
import coinui.composeapp.generated.resources.your_week_plan_is_about_to_expire
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeekPlanExtendOrChangeBottomSheetContent(
    onExtendOneMoreWeek: () -> Unit,
    onSwitchToMonthlyPlan: ()->Unit,
    onNotNow: ()->Unit,
    isDarkMode: Boolean,
) {


    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color(0xff667085))
                )
            }


            Spacer(Modifier.height(16.dp))




            Text(
                text = stringResource(Res.string.your_week_plan_is_about_to_expire),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff101828)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp),
                text = stringResource(Res.string.your_current_plan_ends_tomorrow_dont_lose_premium_features),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )
            Spacer(Modifier.height(24.dp))
            ButtonWithText(
                modifier = Modifier.padding(horizontal = 33.dp),
                text = stringResource(Res.string.extend_for_one_more_week),
                bgColor = PRIMARY,
                textColor = Color.White,
                onClick = onExtendOneMoreWeek
            )
            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 33.dp),
                text = stringResource(Res.string.stay_premium_for_seven_more_days_one_time),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3)
            )

            Spacer(Modifier.height(16.dp))
            ButtonWithText(
                modifier = Modifier.padding(horizontal = 33.dp),
                text = stringResource(Res.string.switch_to_lovorise_monthly_plan),
                bgColor = PRIMARY,
                textColor = Color.White,
                onClick = onSwitchToMonthlyPlan
            )
            Spacer(Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 33.dp),
                text = stringResource(Res.string.enjoy_uninterrupted_access_with_a_better_value),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3)
            )

            Spacer(Modifier.height(16.dp))
            ButtonWithText(
                modifier = Modifier.padding(horizontal = 33.dp),
                text = stringResource(Res.string.not_now),
                bgColor = DISABLED_LIGHT,
                textColor = Color(0xff344054),
                onClick = onNotNow
            )


            Spacer(Modifier.height(14.dp))

        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }


}


