package com.lovorise.app.reels.presentation.reels_create_upload_view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.choose_who_can_view_your_reel
import coinui.composeapp.generated.resources.done
import coinui.composeapp.generated.resources.edit_privacy_settings
import coinui.composeapp.generated.resources.everyone
import coinui.composeapp.generated.resources.everyone_on_lovorise
import coinui.composeapp.generated.resources.ic_locked
import coinui.composeapp.generated.resources.matches
import coinui.composeapp.generated.resources.who_i_liked
import coinui.composeapp.generated.resources.your_liked_people_on_lovorise
import coinui.composeapp.generated.resources.your_matches_on_lovorise
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ReelSettingsBottomSheetContent(isDarkMode:Boolean, privacySettings: ReelPrivacySetting, onPrivacySettingsChanged:(ReelPrivacySetting)->Unit, onDone:()->Unit,isLocked:Boolean,onLockClick:()->Unit) {

   // var isPurchased by remember { mutableStateOf(false) }

    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                text = stringResource(Res.string.edit_privacy_settings),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = stringResource(Res.string.choose_who_can_view_your_reel),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))



            EditPrivacyItem(
                title = stringResource(Res.string.everyone),
                description = stringResource(Res.string.everyone_on_lovorise),
                isChecked = privacySettings == ReelPrivacySetting.Everyone,
                onLockClick = onLockClick,
                isLocked = false,
                onClick = {
                    onPrivacySettingsChanged(ReelPrivacySetting.Everyone)
                },
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(16.dp))

            EditPrivacyItem(
                title = stringResource(Res.string.matches),
                description = stringResource(Res.string.your_matches_on_lovorise),
                isChecked = privacySettings == ReelPrivacySetting.Matches,
                onLockClick = onLockClick,
                isLocked = isLocked,
                onClick = {
                    if (isLocked) {
                        onDone()
                        onLockClick()
                    }else{
                        onPrivacySettingsChanged(ReelPrivacySetting.Matches)
                    }
                },
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(16.dp))


            EditPrivacyItem(
                title = stringResource(Res.string.who_i_liked),
                description = stringResource(Res.string.your_liked_people_on_lovorise),
                isChecked = privacySettings == ReelPrivacySetting.WhoILiked,
                onLockClick = onLockClick,
                isLocked = isLocked,
                onClick = {
                    if (isLocked) {
                        onDone()
                        onLockClick()
                    }else {
                        onPrivacySettingsChanged(ReelPrivacySetting.WhoILiked)
                    }
                },
                isDarkMode = isDarkMode
            )


            Spacer(modifier = Modifier.height(37.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xffEAECF0)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithText(
                text = stringResource(Res.string.done),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = onDone,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.height(16.dp))

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
fun EditPrivacyItem(
    title: String,
    description: String,
    onClick: () -> Unit,
    isLocked:Boolean = false,
    onLockClick:()->Unit,
    isChecked:Boolean,
    isDarkMode: Boolean
) {


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick)
                .weight(1f)
        ) {
            Text(
                text = title,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.2.sp
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = description,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp
            )

        }

        if (isLocked){
            Box(Modifier.size(24.dp).noRippleClickable(onLockClick), contentAlignment = Alignment.Center){
                Image(
                    imageVector = vectorResource(Res.drawable.ic_locked),
                    contentDescription = null,
                    modifier = Modifier.width(16.dp).height(21.dp)
                )
            }
        }

        if (!isLocked){
            Box(modifier = Modifier.size(24.dp).noRippleClickable(onClick), contentAlignment = Alignment.Center) {
                Checkbox(
                    checked = isChecked,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xffF33358),
                        checkmarkColor = if (isDarkMode) BASE_DARK else Color.White,
                        uncheckedColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3)
                    ),
                    onCheckedChange = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

    }


}

enum class ReelPrivacySetting(private val displayName: String) {
    Matches("matches"),
    Everyone("everyone"),
    WhoILiked("whoILiked");

    fun formatted():String = displayName
}