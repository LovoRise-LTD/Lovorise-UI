package com.lovorise.app.lovorise_hearts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.SECONDARY
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

enum class SelectedDateType{
    START,END
}



@Composable
fun FilterTransactionBottomSheetContent(isDarkMode:Boolean,onApply:()->Unit) {

    var selectedDateType by rememberSaveable { mutableStateOf(SelectedDateType.START) }
    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }
    var showTransactionTypeContent by rememberSaveable { mutableStateOf(false) }



    Column {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(Modifier.height(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .height(2.dp)
                        .width(40.dp)
                        .background(Color(0xff667085))
                )
            }


            Spacer(Modifier.height(10.dp))

            TransactionType {
                showTransactionTypeContent = !showTransactionTypeContent
            }

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(if(showTransactionTypeContent) Res.string.transaction_type else Res.string.choose_time),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))

            if (showTransactionTypeContent){
                TransactionTypeContent(onApply = onApply)
            }else{
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SelectedDates(Modifier.weight(1f),isSelected = selectedDateType == SelectedDateType.START,onClick = {selectedDateType = SelectedDateType.START},date = startDate)
                    SelectedDates(Modifier.weight(1f),isSelected = selectedDateType == SelectedDateType.END,onClick = {selectedDateType = SelectedDateType.END},date = endDate)
                }

                Spacer(Modifier.height(16.dp))

                DatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    selectedYear = {},
                    selectedMonth = {},
                    selectedDay = {},
                )
            }

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.apply),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = onApply

            )


            Spacer(Modifier.height(22.dp))




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) Color.Black else Color.White)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionTypeContent(onApply:()->Unit) {
    val items = stringArrayResource(Res.array.transaction_types)
    var selectedItem by rememberSaveable{ mutableStateOf("") }
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items.forEach { item ->
            SelectTransactionType(item = item,isSelected = item == selectedItem,onClick = {
                selectedItem = item
                onApply()
            })
        }

    }
}


@Composable
fun SelectTransactionType(item:String,onClick:()->Unit,isSelected:Boolean) {
    Box(Modifier.height(29.dp).noRippleClickable(onClick).background(if (isSelected) SECONDARY else Color(0xffF2F4F7), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = item,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) PRIMARY else Color(0xff344054)
        )
    }
}

@Composable
fun SelectedDates(modifier: Modifier,isSelected:Boolean,onClick:()->Unit,date:String) {
    Box(modifier.height(57.dp).border(1.dp,if (isSelected) PRIMARY else DISABLED_LIGHT).noRippleClickable(onClick), contentAlignment = Alignment.CenterStart) {
        Column(Modifier.padding(horizontal = 16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(Res.string.start_date),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff475467)
            )

            Text(
                text = date,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff667085)
            )
        }
    }
}

@Composable
fun TransactionType(onClick:()->Unit) {
    Row(Modifier.height(29.dp).noRippleClickable(onClick).background(SECONDARY, RoundedCornerShape(16.dp)), verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(16.dp))
        Text(
            text = stringResource(Res.string.transaction_type),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = PRIMARY
        )
        Spacer(Modifier.width(4.dp))

        Icon(
            imageVector = vectorResource(Res.drawable.ic_chev_down),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = PRIMARY
        )

    }
}