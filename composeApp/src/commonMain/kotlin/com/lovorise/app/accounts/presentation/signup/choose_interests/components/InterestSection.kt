package com.lovorise.app.accounts.presentation.signup.choose_interests.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.show_less
import coinui.composeapp.generated.resources.show_more
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreenState
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestSection(
    item:ChooseInterestsScreenState.InterestCategoryWithItems,
    onChipClicked:(String)->Unit,
    isSelected:(String)->Boolean,
    showLess: Boolean,
    onShowLess:()->Unit,
    onShowMore:()->Unit,
    isDarkMode:Boolean,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Text(
            text = item.category,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 24.sp,
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

        Spacer(Modifier.height(12.dp))

        FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
            getItems(showLess, item).forEach {value ->
                InterestChips(text = value.name, isSelected = isSelected(value.name), onClick = {onChipClicked(value.name)},isDarkMode = isDarkMode)
            }
        }

        Spacer(Modifier.height(16.dp))

        if (item.items.size > 10) {
            Box(
                modifier = Modifier.height(40.dp).fillMaxWidth().align(Alignment.CenterHorizontally)
                    .noRippleClickable {
                        if (showLess) onShowMore() else onShowLess()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showLess) stringResource(Res.string.show_more) else stringResource(Res.string.show_less),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = (-0.3).sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
            }
        }


    }





}

fun getItems(showLess:Boolean,item:ChooseInterestsScreenState.InterestCategoryWithItems):List<ChooseInterestsScreenState.InterestCategoryWithItems.Item>{
    if (!showLess || item.items.size <= 10) return item.items
    return item.items.take(10)
}