package com.lovorise.app.bottom_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_nav_1
import coinui.composeapp.generated.resources.ic_nav_1_active
import coinui.composeapp.generated.resources.ic_nav_1_active_dark
import coinui.composeapp.generated.resources.ic_nav_1_dark
import coinui.composeapp.generated.resources.ic_nav_3
import coinui.composeapp.generated.resources.ic_nav_3_active
import coinui.composeapp.generated.resources.ic_nav_4
import coinui.composeapp.generated.resources.ic_nav_4_active
import coinui.composeapp.generated.resources.ic_nav_5
import coinui.composeapp.generated.resources.ic_nav_5_active
import coinui.composeapp.generated.resources.ic_nav_5_dark
import com.lovorise.app.home.TabsScreenModel.BottomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.components.CircularProfileIcon
import com.lovorise.app.profile.presentation.components.SpotlightType
import com.lovorise.app.ui.BASE_DARK
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BottomNavBar(activeTab:BottomTab, onClick:(BottomTab)->Unit, height: Dp = 48.dp,isSpotlightEnabled:Boolean,profileUrl:String,isDarkMode:Boolean,progress:Float,spotlightType: SpotlightType?) {


    Row(
        modifier = Modifier.fillMaxWidth().height(height).padding(horizontal = 20.dp).background(if (isDarkMode) BASE_DARK else Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Image(
            imageVector = vectorResource(if (activeTab == BottomTab.SWIPE){ if (isDarkMode) Res.drawable.ic_nav_1_active_dark else Res.drawable.ic_nav_1_active} else { if (isDarkMode) Res.drawable.ic_nav_1_dark else Res.drawable.ic_nav_1 }),
            contentDescription = "bottom nav icon",
            modifier = Modifier.size(20.dp).noRippleClickable {
                onClick(BottomTab.SWIPE)
            }
        )
        Spacer(Modifier.weight(1f))

//        Image(
//            imageVector = vectorResource(if (activeTab == BottomTab.EVENTS) Res.drawable.ic_nav_2_active else Res.drawable.ic_nav_2),
//            contentDescription = "bottom nav icon",
//            modifier = Modifier.size(24.dp).noRippleClickable {
//              //  onClick(1)
//            }
//        )
//        Spacer(Modifier.weight(1f))

        Image(
            imageVector = vectorResource(if (activeTab == BottomTab.REELS) Res.drawable.ic_nav_3_active else Res.drawable.ic_nav_3),
            contentDescription = "bottom nav icon",
            modifier = Modifier.width(15.86.dp).height(18.75.dp).noRippleClickable {
                onClick(BottomTab.REELS)
            }
        )
        Spacer(Modifier.weight(1f))

        Image(
            imageVector = vectorResource(if (activeTab == BottomTab.CHAT) Res.drawable.ic_nav_4_active else Res.drawable.ic_nav_4),
            contentDescription = "bottom nav icon",
            modifier = Modifier.size(20.dp).noRippleClickable {
                onClick(BottomTab.CHAT)
            }
        )
        Spacer(Modifier.weight(1f))




        // this can be used in case if we are not using using spotlight
        if (!isSpotlightEnabled) {
            Image(
                imageVector = vectorResource(if (activeTab == BottomTab.PROFILE) Res.drawable.ic_nav_5_active else if (isDarkMode) Res.drawable.ic_nav_5_dark else Res.drawable.ic_nav_5),
                contentDescription = "bottom nav icon",
                modifier = Modifier.size(18.dp).noRippleClickable {
                    onClick(BottomTab.PROFILE)
                }
            )
        } else {
            if (spotlightType != null){
                CircularProfileIcon(
                    onClick = {
                        onClick(BottomTab.PROFILE)
                    },
                    imageUrl = profileUrl,
                    completionPercent = progress*100,
                    iconSize = 24.dp,
                    borderWidth = 1.5.dp,
                    spotlightType = spotlightType
                )
            }

        }









    }
}