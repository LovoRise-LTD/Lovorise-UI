package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.continue_editing
import coinui.composeapp.generated.resources.discard_post
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.your_reel_is_not_saved
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.noRippleClickable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DiscardPostConfirmationDialog(
    onCancel:()->Unit,
    onDiscard:()->Unit
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
                .padding(horizontal = 17.dp)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Box(
                    modifier = Modifier.padding(end = 22.dp).size(24.dp).align(Alignment.End).noRippleClickable(onCancel),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(Res.string.discard_post)+ "?",
                    color = Color(0xff101828),
                    lineHeight = 20.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.your_reel_is_not_saved),
                    color = Color(0xff475467),
                    lineHeight = 21.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    modifier = Modifier.noRippleClickable(onCancel),
                    text = stringResource(Res.string.continue_editing),
                    color = Color(0xffF33358),
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xffEAECF0)))
                Spacer(Modifier.height(8.dp))

                Text(
                    modifier = Modifier.noRippleClickable(onDiscard),
                    text = stringResource(Res.string.discard_post),
                    color = Color(0xff101828),
                    lineHeight = 24.sp,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center
                )


            }

        }
    }
}