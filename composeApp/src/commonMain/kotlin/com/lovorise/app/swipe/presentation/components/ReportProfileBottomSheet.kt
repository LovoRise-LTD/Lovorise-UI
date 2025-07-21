package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource


data class ReportCheckBoxItem(
    val description:String,
    val isChecked: Boolean
)

@Composable
fun ReportProfileBottomSheet(
    isDarkMode:Boolean,
    onReportButtonClick:()->Unit
) {

    val reasonsRes = stringArrayResource(Res.array.report_profile_reasons)

    var checkBoxItems by remember { mutableStateOf(
        reasonsRes.map { ReportCheckBoxItem(it,false) }
    ) }


    var isButtonEnabled by remember { mutableStateOf(false) }
    var checkedItemsSize by remember { mutableIntStateOf(0) }

    LaunchedEffect(checkBoxItems){
        val checkedItems = checkBoxItems.filter { it.isChecked }
        checkedItemsSize = checkedItems.size
        isButtonEnabled = checkedItems.isNotEmpty()
    }



    BoxWithConstraints {
        Column(
            modifier = Modifier.then(if (maxHeight< 560.dp) Modifier.fillMaxHeight(0.9f) else Modifier)
        ) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
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
                    text = stringResource(Res.string.report),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    color = Color(0xff101828)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(Res.string.please_select_reasons_behind_report),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    color = Color(0xff344054)
                )
                Spacer(Modifier.height(16.dp))

                checkBoxItems.forEachIndexed { index, item ->
                    ReportReasonItem(item.description,item.isChecked, onCheckBoxClick = {
                        if (item.isChecked || (checkedItemsSize < 3)) {
                            checkBoxItems = checkBoxItems.toMutableList().apply {
                                add(
                                    index,
                                    removeAt(index).copy(isChecked = !item.isChecked)
                                )
                            }
                        }
                    })
                    Spacer(Modifier.height(16.dp))
                }


                Spacer(Modifier.height(16.dp))
                ButtonWithText(
                    text = stringResource(Res.string.report),
                    bgColor = Color(if (isButtonEnabled) 0xffF33358 else 0xffEAECF0),
                    textColor = Color(if (isButtonEnabled) 0xffffffff else 0xff98A2B3),
                    onClick = {
                        if (isButtonEnabled){
                            onReportButtonClick()
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))




            }

            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .fillMaxWidth()
                    .background(if (isDarkMode) Color.Black else Color.White)
            )


        }
    }

}

@Composable
fun ReportReasonItem(text:String,isChecked:Boolean,onCheckBoxClick:()->Unit) {

    Row(
        modifier = Modifier.height(24.dp).noRippleClickable(onCheckBoxClick).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            Checkbox(
                checked = isChecked,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xffF33358),
                    checkmarkColor = Color.White,
                    uncheckedColor = Color(0xffD0D5DD)
                ),
                onCheckedChange = null,
                modifier = Modifier.size(width = 14.61.dp, height = 15.dp)
            )
        }

        Spacer(Modifier.width(13.39.dp))

        Text(
            text = text,
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 24.sp,
            color = Color(0xff101828)
        )

    }


}