package com.lovorise.app.profile.presentation.edit_profile.components.sheets_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coinui.composeapp.generated.resources.how_tall_are_you
import coinui.composeapp.generated.resources.ic_height_scale
import coinui.composeapp.generated.resources.save
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.height.HeightPickerWheel
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.components.CheckboxAndText
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.abs

@Composable
fun UpdateHeightSheetContent(
    height:EditProfileScreenState.Height,
    updateHeight:(EditProfileScreenState.Height)->Unit,
    onSave: () -> Unit,
    isDarkMode: Boolean
) {

    val isButtonEnabled by remember { mutableStateOf(true) }

    var newHeightIndex by remember { mutableIntStateOf(height.index) }

    var isVisible by remember { mutableStateOf(height.isVisible) }


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


            Spacer(Modifier.height(10.dp))

            Icon(
                tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                imageVector = vectorResource(Res.drawable.ic_height_scale),
                contentDescription = null,
                modifier = Modifier.width(26.93.dp).height(24.4.dp)
                    .align(Alignment.CenterHorizontally)
            )


            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.how_tall_are_you),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(28.dp))

            HeightPickerWheel(
                startIndex = height.index,
                isDarkMode = isDarkMode,
                onChange = {
                    newHeightIndex = abs(it-90)
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            CheckboxAndText(
                isChecked = isVisible,
                onClick = {
                    isVisible = !isVisible
                },
                isDarkMode = isDarkMode
            )

            Spacer(modifier = Modifier.height(16.dp))

            ButtonWithText(
                text = stringResource(Res.string.save),
                bgColor = if (isButtonEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (isButtonEnabled) {
                        updateHeight(height.copy(height = newHeightIndex+90, isVisible = isVisible, index = newHeightIndex))
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