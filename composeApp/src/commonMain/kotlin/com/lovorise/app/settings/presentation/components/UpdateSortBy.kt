package com.lovorise.app.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.sort_by
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateSortBySheetContent(
    sortBy: EditProfileScreenState.SortBy,
    isDarkMode:Boolean,
    onSave:()->Unit,
    updateSortBy:(EditProfileScreenState.SortBy)->Unit,
    buttonText:String = stringResource(Res.string.save)
) {


    var isButtonEnabled by remember { mutableStateOf(true) }
    var selectedIndex by remember { mutableIntStateOf(sortBy.selectedIndex) }

    LaunchedEffect(selectedIndex){
        isButtonEnabled = selectedIndex >= 0
    }



    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {

            Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color(0xff667085))
                )
            }


           // Spacer(Modifier.height(16.dp))

//            Image(
//                imageVector = vectorResource(Res.drawable.ic_sort_by),
//                contentDescription = null,
//                modifier = Modifier.width(17.88.dp).height(24.88.dp)
//                    .align(Alignment.CenterHorizontally)
//            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.sort_by),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff344054)
            )

            Spacer(Modifier.height(30.dp))



            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically){
                sortBy.items.forEachIndexed { index, value ->
                    ElevatedChipItem1(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        text = value,
                        isSelected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        isDarkMode = isDarkMode
                    )
                }
            }


            Spacer(modifier = Modifier.height(53.dp))


            ButtonWithText(
                text = buttonText,
                bgColor = if (isButtonEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isButtonEnabled) {
                        updateSortBy(sortBy.copy(selectedIndex = selectedIndex))
                        onSave()
                    }
                }
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

@Composable
fun ElevatedChipItem1(
    modifier: Modifier= Modifier,
    text:String,
    isSelected:Boolean,
    onClick:()->Unit,
    isDarkMode: Boolean
) {
    Card(
        modifier = modifier
            .height(33.dp)
            .noRippleClickable(onClick),
      //  elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp,if (isSelected) Color(0xffF33358) else { if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0) }),
        colors = CardDefaults.cardColors(if (isSelected) Color(0xffF33358) else { if (isDarkMode) CARD_BG_DARK else Color(0xffffffff) })
    ){
        Box(Modifier.fillMaxHeight().fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = text,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (!isSelected) { if (isDarkMode) DISABLED_LIGHT else  Color(0xff667085) } else Color.White
            )
        }
    }
}