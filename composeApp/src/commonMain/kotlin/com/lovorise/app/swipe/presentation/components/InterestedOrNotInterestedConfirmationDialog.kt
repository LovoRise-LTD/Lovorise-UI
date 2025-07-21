package com.lovorise.app.swipe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.*
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource

@Composable
fun InterestedOrNotInterestedConfirmationDialog(
    onPositive:()->Unit,
    onCancel:()->Unit,
    isInterested:Boolean,
    isSkip:Boolean = false
) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .background(Color.White, shape = RoundedCornerShape(15.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp),
                   // .padding(top = 29.dp, bottom = 22.dp),
                verticalArrangement = Arrangement.Top,
              //  horizontalAlignment = Alignment.CenterStart
            ) {

                Spacer(Modifier.height(20.dp))
                Text(
                    text = if (isSkip) stringResource(Res.string.temp_skip) else if (isInterested) stringResource(Res.string.interested) else "${stringResource(Res.string.not_interested)}?",
                    color = Color(0xff101828),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = if (isSkip) stringResource(Res.string.temp_skip_profile_message) else if (isInterested) stringResource(Res.string.swipe_profile_right_message) else stringResource(Res.string.swipe_profile_left_message),
                    color = Color(0xff344054),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(Modifier.weight(1f))
                    Text(
                        modifier = Modifier.noRippleClickable{onCancel()},
                        text = stringResource(Res.string.cancel),
                        color = Color(0xff667085),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        lineHeight = 21.sp,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp
                    )

                    Text(
                        modifier = Modifier.noRippleClickable{onPositive()},
                        text = if (isInterested || isSkip) stringResource(Res.string.yes) else stringResource(Res.string.not_interested),
                        color = Color(0xff667085),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        lineHeight = 21.sp,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp
                    )

                }


                Spacer(Modifier.height(20.dp))




            }







        }



    }

}