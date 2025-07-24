package com.lovorise.app.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.boost_profile_visibility_for_hours
import coinui.composeapp.generated.resources.boost_visibility_for
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.hours
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.ic_spotlight
import coinui.composeapp.generated.resources.ic_super_spotlight
import coinui.composeapp.generated.resources.spotlight
import coinui.composeapp.generated.resources.spotlight_profile
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SpotlightsSheetContent(
    spotlightHearts:Int,
    superSpotlightCoins:Int,
    onCancelClick:()->Unit,
    changeSystemNavColor:()->Unit,
    isDarkMode: Boolean,
    screenWidth:Dp,
    onSpotlight:(SpotlightType) ->Unit
) {

    LaunchedEffect(isDarkMode){
        println("is dark mode $isDarkMode")
        changeSystemNavColor()
    }

    var superSpotlightSelected by remember { mutableStateOf(false) }

    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(
                    horizontal = if (screenWidth <= 340.dp)  5.dp else if (screenWidth in 340.dp..380.dp) 10.dp else 15.dp
                )
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                // .padding(horizontal = 25.dp, vertical = 25.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    //text = if (superSpotlightSelected) stringResource(Res.string.super_spotlights) else stringResource(Res.string.spotlights),
                    text = stringResource(Res.string.spotlight_profile),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) Color.White else Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_heart_small),
                        contentDescription = "hearts",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = (if (superSpotlightSelected) superSpotlightCoins else spotlightHearts).toString(),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        lineHeight = 27.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                SpotlightItem(
                    text = stringResource(Res.string.spotlight),
                    spotlightRes = Res.drawable.ic_spotlight,
                    coins = superSpotlightCoins,
                    modifier = Modifier.fillMaxWidth().height(129.dp).noRippleClickable { superSpotlightSelected = true },
                    visibilityHrs = 24,
                    borderColor = Color(0XFFF33358),
//                    borderColor = if (superSpotlightSelected) Color(0XFFF33358) else Color(0xffEAECF0),
                    borderWidth = 1.dp,
//                    borderWidth = if (superSpotlightSelected) 1.dp else 1.3.dp,
                    isDarkMode = isDarkMode
                )

//                Spacer(Modifier.width(
//                  if (screenWidth <= 340.dp)  3.dp else if (screenWidth in 340.dp..380.dp) 10.dp else 15.dp
//                ))
//
//                SpotlightItem(
//                    text = stringResource(Res.string.spotlight),
//                    spotlightRes = Res.drawable.ic_spotlight,
//                    coins = spotlightHearts,
//                    modifier =Modifier.aspectRatio(166f/148f).weight(1f).noRippleClickable { superSpotlightSelected = false },
//                    visibilityHrs = 1,
//                    borderColor =  if (!superSpotlightSelected) Color(0XFFF33358) else Color(0xffEAECF0),
//                    borderWidth = if (!superSpotlightSelected) 1.dp else 1.3.dp,
//                    isDarkMode = isDarkMode
//                )

            }



            Spacer(Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0XFFF33358), RoundedCornerShape(50))
                    .noRippleClickable { onCancelClick(); onSpotlight(if (superSpotlightSelected) SpotlightType.SUPER_SPOTLIGHT else SpotlightType.SPOTLIGHT) },
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = stringResource(Res.string.spotlight),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.White,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp
                )

            }


            Text(
                text = stringResource(Res.string.cancel),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xff98A2B3),
                lineHeight = 27.sp,
                letterSpacing = 0.2.sp,
                modifier = Modifier.noRippleClickable { onCancelClick() }
            )

//        Spacer(
//            Modifier.windowInsetsBottomHeight(
//                WindowInsets.navigationBars
//            )
//        )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )
    }


}

@Composable
fun SpotlightItem(modifier: Modifier,text:String,coins:Int,visibilityHrs:Int,spotlightRes:DrawableResource,borderColor: Color,borderWidth:Dp,isDarkMode: Boolean) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Card(
            modifier = Modifier.fillMaxSize(),
            border = BorderStroke(
                width = borderWidth,
                color = borderColor
            ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)

        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = text,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp
                )

                Image(
                    imageVector = vectorResource(spotlightRes),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    Image(
                        imageVector = vectorResource(Res.drawable.ic_heart_small),
                        contentDescription = "coins",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(10.dp))

                    Text(
                        modifier = Modifier,
                        text = coins.toString(),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color.Black
                    )


                }
                Spacer(modifier = Modifier.weight(1f))

            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.boost_profile_visibility_for_hours),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0XFF667085),
//            lineHeight = 16.5.sp,
            letterSpacing = 0.2.sp
        )


    }


}