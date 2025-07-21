package com.lovorise.app.profile_visitors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.PRIMARY


@Composable
fun CustomTabs(
    tabs:List<String>,
    activeIndex:Int,
    onTabSelected: (Int) -> Unit,
    isDarkMode:Boolean,
    modifier: Modifier = Modifier,
    activeTextColor: Color = Color(0xffF33358),
    inactiveTextColor: Color = Color(0xff98A2B3),
    activeTabIndicatorColor:Color = Color(0xffF33358),
    inactiveTabIndicatorColor:Color = Color(0xffEAECF0),
) {

    if (tabs.size != 2) throw Exception("2 are required")

    @Composable
    fun TabItem(modifier: Modifier,text:String,isActive:Boolean) {
        Column(modifier.fillMaxHeight().fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = text,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = if (isActive) activeTextColor else inactiveTextColor,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )

            Box(Modifier.fillMaxWidth().height(2.dp).background(if(isActive) activeTabIndicatorColor else{ if (isDarkMode) CARD_BG_DARK else inactiveTabIndicatorColor}))

        }
    }

    Row (
        modifier = modifier.fillMaxWidth().height(36.dp),
    ){
        TabItem(
            modifier = Modifier.weight(1f).noRippleClickable { onTabSelected(0) },
            text = tabs.first(),
            isActive = activeIndex == 0
        )
        TabItem(
            modifier = Modifier.weight(1f).noRippleClickable { onTabSelected(1) },
            text = tabs.last(),
            isActive = activeIndex == 1
        )

    }
    
}

@Composable
fun CustomTabRow(
    modifier: Modifier = Modifier,
    titles: List<String>,
    tabIndex: Int,
    count1: Int? = null,
    count2: Int? = null,
    containerColor: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(top = 20.dp),
    onTabSelected: (Int) -> Unit,
    isDarkMode: Boolean
) {
    TabRow(
        modifier = modifier
            .padding(paddingValues = paddingValues),
        selectedTabIndex = tabIndex,
        containerColor = containerColor,
        contentColor = Color(0xffF33358),
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                color = Color(0xffF33358)
            )
        },
        divider = { HorizontalDivider(color = Color(0xFFEAECF0)) }
    ) {
        titles.forEachIndexed { index, title ->
            CustomTab(
                title = title,
                count = if (index == 0) count1 else count2,
                selected = tabIndex == index,
                onClick = { onTabSelected(index) },
                isDarkMode = isDarkMode
            )
        }
    }
}

@Composable
private fun CustomTab(
    title: String,
    count: Int? = null,
    selected: Boolean,
    onClick: () -> Unit,
    isDarkMode: Boolean
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(bottom = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                color = if (selected) Color(0xffF33358) else {
                   if (isDarkMode) Color.White else Color(0xff667085)
                },
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium
            )

            if (count != null && count > 0) {
                Box(
                    modifier = Modifier.padding(start = 8.dp).background(if (selected) {
                       if (isDarkMode) PRIMARY else Color(0xFFF9E9EC)
                    }else {
                        if (isDarkMode) Color(0xff737272) else Color(0xFFF2F4F7)
                    },RoundedCornerShape(16.dp)),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        text = count.toString(),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = if (selected) {
                            if (isDarkMode) Color.White else Color(0xffF33358)
                        } else {
                            if (isDarkMode) Color.White else Color(0XFF344054)
                        },
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}