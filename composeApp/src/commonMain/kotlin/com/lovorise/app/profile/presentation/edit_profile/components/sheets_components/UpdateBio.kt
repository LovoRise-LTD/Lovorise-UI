package com.lovorise.app.profile.presentation.edit_profile.components.sheets_components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.description
import coinui.composeapp.generated.resources.ic_bio
import coinui.composeapp.generated.resources.my_bio
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.write_something
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.onboarding_info.DescriptionTextField
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


@Composable
fun UpdateBioSheetContent(
    bio: EditProfileScreenState.Bio,
    updateBio:(EditProfileScreenState.Bio)->Unit,
    onSave:() -> Unit,
    isDarkMode:Boolean
) {

    var isVisible by remember { mutableStateOf(bio.isVisibleOnProfile) }
    var description by remember { mutableStateOf(bio.description) }

    var isButtonEnabled by remember { mutableStateOf(bio.charCount >= 2) }

    LaunchedEffect(description){
        isButtonEnabled = description.length in 2..500
    }

    BoxWithConstraints {
        val height = maxHeight
        Column(
            modifier = Modifier.then(
                if(height < 550.dp) Modifier.fillMaxHeight(0.9f) else Modifier
            )
        ) {
            Column(
                modifier = Modifier
                    .background(if (isDarkMode) BASE_DARK else Color.White)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
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


                Spacer(Modifier.height(10.dp))

                Box(Modifier.size(32.dp).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                    Icon(
                        tint = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
                        imageVector = vectorResource(Res.drawable.ic_bio),
                        contentDescription = null,
                        modifier = Modifier.width(23.33.dp).height(26.67.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                    text = stringResource(Res.string.my_bio),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 27.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )




                Text(
                    text = stringResource(Res.string.description),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(modifier = Modifier.height(6.dp))

                DescriptionTextField(
                    value = description,
                    onValueChange = { if (it.length <= 500) description = it},
                    label = stringResource(Res.string.write_something),
                    height = if(height < 550.dp) 140.dp else 168.dp,
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
                    ),
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    bgColor = if (isDarkMode) BASE_DARK else Color.White,

                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${bio.charCount}/500",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                CheckboxAndText(onClick = {isVisible = !isVisible}, isChecked = isVisible, isDarkMode = isDarkMode)

                Spacer(modifier = Modifier.height(16.dp))

                ButtonWithText(
                    text = stringResource(Res.string.save),
                    bgColor = if (isButtonEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isButtonEnabled) {
                            if (description.isNotBlank()) {
                                updateBio(
                                    EditProfileScreenState.Bio(
                                        description = description,
                                        charCount = description.length,
                                        isVisibleOnProfile = isVisible
                                    )
                                )
                            } else{
                                if (bio.isVisibleOnProfile != isVisible){
                                    updateBio(bio.copy(isVisibleOnProfile = isVisible))
                                }
                            }

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




}