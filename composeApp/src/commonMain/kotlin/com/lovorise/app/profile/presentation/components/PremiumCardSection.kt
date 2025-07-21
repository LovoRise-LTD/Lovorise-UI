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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.get_from
import coinui.composeapp.generated.resources.manage
import coinui.composeapp.generated.resources.next_payment
import coinui.composeapp.generated.resources.subscribed_to_premium_monthly_message
import coinui.composeapp.generated.resources.subscribed_to_premium_weekly_message
import coinui.composeapp.generated.resources.subscriptions
import coinui.composeapp.generated.resources.unlock_all_features_message
import coinui.composeapp.generated.resources.change_plan
import coinui.composeapp.generated.resources.view_plan
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.SubscriptionType
import org.jetbrains.compose.resources.stringResource


@Composable
fun PremiumCardSection(
    onGetPlanClick : ()->Unit,
    onUpgradePlan: () ->Unit,
    onViewPlan: () -> Unit,
    onManagePlan:() ->Unit,
    currentLovorisePlan: SubscriptionType,
    expires:String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onGetPlanClick)
            .background(brush = Brush.verticalGradient(listOf(Color(0xffF3335D), Color(0xffF33386))), shape = RoundedCornerShape(12.dp)),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(Modifier.height(43.dp), contentAlignment = Alignment.Center){
                Text(
                    text = stringResource(Res.string.subscriptions),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 18.sp,
                    letterSpacing = -(0.3.sp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    lineHeight = 27.sp
                )
            }

            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp),
                text = stringResource(if (currentLovorisePlan == SubscriptionType.FREE) Res.string.unlock_all_features_message else if (currentLovorisePlan == SubscriptionType.WEEKLY) Res.string.subscribed_to_premium_weekly_message else Res.string.subscribed_to_premium_monthly_message),
                fontFamily = PoppinsFontFamily(),
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                lineHeight = 21.sp
            )

            if (currentLovorisePlan == SubscriptionType.MONTHLY) {
                Row (modifier = Modifier.padding(top = 12.dp, bottom = 4.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = stringResource(Res.string.next_payment,expires),
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 21.sp
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        modifier = Modifier.noRippleClickable(onManagePlan),
                        text = stringResource(Res.string.manage),
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 21.sp,
                        textDecoration = TextDecoration.Underline
                    )

                }
            }


            Box(
                modifier = Modifier.padding(top = 8.dp).background(Color.White, shape = RoundedCornerShape(16.dp)).noRippleClickable(if (currentLovorisePlan == SubscriptionType.FREE) onGetPlanClick else if (currentLovorisePlan == SubscriptionType.WEEKLY) onUpgradePlan else onViewPlan)
            ){
                Text(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    text =  stringResource(if (currentLovorisePlan == SubscriptionType.FREE) Res.string.get_from else if (currentLovorisePlan == SubscriptionType.WEEKLY) Res.string.change_plan else Res.string.view_plan),
                    fontFamily = PoppinsFontFamily(),
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xffF33358),
                    lineHeight = 21.sp
                )


            }


        }

    }


}
