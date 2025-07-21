package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.reels.domain.models.MarkReelRequest
import com.lovorise.app.reels.domain.models.ReelStatus
import com.lovorise.app.reels.domain.models.ReportReel
import com.lovorise.app.swipe.presentation.components.ReportCheckBoxItem
import com.lovorise.app.swipe.presentation.components.ReportReasonItem
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReportReelBottomSheetContent(
    reelId:Int,
    isDarkMode:Boolean,
    onReportButtonClick:(ReportReel)->Unit,
    showNotInterestedOptions:Boolean,
    showReportOptions:Boolean,
    onReportClick:()->Unit,
    onNotInterestedClick:(MarkReelRequest)->Unit,
    hideBottomSheet:()->Unit,
    onToastMessage:(String)->Unit
) {

    val fewerReelsMsg = stringResource(Res.string.we_will_show_fewer_reels_like_this)


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

                if (!showReportOptions && !showNotInterestedOptions){
                    NotInterestedAndReport(onReportClick){
                        onNotInterestedClick(MarkReelRequest(reelId,ReelStatus.NOT_INTERESTED,null))
                    }
                }

                if (showNotInterestedOptions){
                    NotInterestedOptions(hideBottomSheet = {
                        hideBottomSheet()
                        onToastMessage(fewerReelsMsg)
                    }, onUndo = hideBottomSheet)
                }

                if (showReportOptions){
                    ReportOptions(onReportButtonClick = {
                        hideBottomSheet()
                        onToastMessage(fewerReelsMsg)
                        onReportButtonClick(ReportReel(reelId,it))
                    })
                }




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
fun ReportOptions(onReportButtonClick: (List<String>) -> Unit) {

    val reportReasons = stringArrayResource(Res.array.report_reels_reasons)
    var checkBoxItems by remember {
        mutableStateOf(reportReasons.map { ReportCheckBoxItem(it,false) })
    }


    var isButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(checkBoxItems){
        isButtonEnabled = checkBoxItems.any { it.isChecked }
    }


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
        text = stringResource(Res.string.please_select_reasons_for_reporting_this_reel),
        fontFamily = PoppinsFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 21.sp,
        color = Color(0xff344054)
    )
    Spacer(Modifier.height(16.dp))

    checkBoxItems.forEachIndexed { index, item ->
        ReportReasonItem(
            item.description,
            item.isChecked,
            onCheckBoxClick = {
                if (checkBoxItems[index].isChecked || checkBoxItems.filter { it.isChecked }.size < 3) {
                    checkBoxItems = checkBoxItems.toMutableList()
                        .apply { add(index, removeAt(index).copy(isChecked = !item.isChecked)) }
                }
            })
        Spacer(Modifier.height(16.dp))
    }


    Spacer(Modifier.height(16.dp))
    ButtonWithText(
        text = stringResource(Res.string.report_reel),
        bgColor = Color(if (isButtonEnabled) 0xffF33358 else 0xffEAECF0),
        textColor = Color(if (isButtonEnabled) 0xffffffff else 0xff98A2B3),
        onClick = {
            if (isButtonEnabled){
                onReportButtonClick(checkBoxItems.filter { it.isChecked }.map { it.description })
            }
        }
    )
}


@Composable
fun NotInterestedOptions(hideBottomSheet: () -> Unit,onUndo:()->Unit) {

    Text(
        text = stringResource(Res.string.not_interested),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
        color = Color(0xff101828),
        letterSpacing = 0.2.sp,
        fontFamily = PoppinsFontFamily(),
    )

    Spacer(Modifier.height(16.dp))

    Text(
        modifier = Modifier.noRippleClickable(hideBottomSheet),
        text = stringResource(Res.string.i_do_not_want_to_see_reels_from),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = Color(0xff101828),
        letterSpacing = 0.2.sp,
        fontFamily = PoppinsFontFamily(),
    )

    Spacer(Modifier.height(16.dp))

    Text(
        modifier = Modifier.noRippleClickable(hideBottomSheet),
        text = stringResource(Res.string.this_reel_make_me_uncomfortable),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = Color(0xff101828),
        letterSpacing = 0.2.sp,
        fontFamily = PoppinsFontFamily(),
    )

    Spacer(Modifier.height(16.dp))

    Text(
        modifier = Modifier.noRippleClickable(onUndo),
        text = stringResource(Res.string.undo),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = Color(0xffF33358),
        letterSpacing = 0.2.sp,
        fontFamily = PoppinsFontFamily(),
    )

}


@Composable
fun NotInterestedAndReport(onReportClick: () -> Unit,onNotInterestedClick: () -> Unit) {

    Box(modifier = Modifier.height(44.dp).fillMaxWidth().noRippleClickable{onNotInterestedClick()}, contentAlignment = Alignment.CenterStart) {
        Text(
            text = stringResource(Res.string.not_interested),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            color = Color(0xff101828),
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )
    }

    Spacer(Modifier.height(8.dp))

    Box(modifier = Modifier.height(44.dp).fillMaxWidth().noRippleClickable{onReportClick()}, contentAlignment = Alignment.CenterStart) {
        Text(
            text = stringResource(Res.string.report),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            color = Color(0xffF33358),
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
        )
    }

}