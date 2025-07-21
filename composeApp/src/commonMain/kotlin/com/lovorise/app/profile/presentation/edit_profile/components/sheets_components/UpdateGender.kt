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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import coinui.composeapp.generated.resources.add_more_about_your_gender
import coinui.composeapp.generated.resources.aware_gender_update
import coinui.composeapp.generated.resources.describe_here
import coinui.composeapp.generated.resources.gender
import coinui.composeapp.generated.resources.genders
import coinui.composeapp.generated.resources.ic_person
import coinui.composeapp.generated.resources.more_gender_options
import coinui.composeapp.generated.resources.pick_gender_that_describe_you
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.save_and_close
import coinui.composeapp.generated.resources.tell_something_missing
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.gender.AddMoreAboutGender
import com.lovorise.app.accounts.presentation.signup.gender.TellUsMoreComponent
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.noRippleClickable
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.components.CheckboxAndText
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun UpdateGenderSheetContent(
    gender: EditProfileScreenState.Gender,
    onSave: () -> Unit,
    updateGender: (EditProfileScreenState.Gender)->Unit,
    isDarkMode: Boolean,
    shouldDisable:Boolean
) {

    var showMoreOption by rememberSaveable { mutableStateOf(false) }
    var showSomethingMissingOption by rememberSaveable { mutableStateOf(false) }

    Column {

        if (showMoreOption){
            UpdateMoreAboutYourGender(
                gender = gender.copy(selectedIndex = 2),
                onSave = onSave,
                navigateToAddMore = {
                    showMoreOption = false
                    showSomethingMissingOption = true
                },
                updateGender = updateGender,
                shouldDisable = shouldDisable,
                isDarkMode = isDarkMode
            )
        }else{
            if (!showSomethingMissingOption) {
                SelectGenderContent(
                    gender = gender,
                    updateGender = updateGender,
                    onSave = onSave,
                    onAddMore = {
                        showSomethingMissingOption = false
                        showMoreOption = true
                    },
                    shouldDisable = shouldDisable,
                    isDarkMode = isDarkMode
                )
            }
        }

        if (showSomethingMissingOption){
            ShowSomethingMissingContent(
                gender = gender.copy(selectedIndex = 2),
                updateGender = updateGender,
                onSave = onSave,
                shouldDisable = shouldDisable,
                isDarkMode = isDarkMode
            )
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
fun ShowSomethingMissingContent(shouldDisable: Boolean,gender: EditProfileScreenState.Gender,updateGender: (EditProfileScreenState.Gender)->Unit,onSave: () -> Unit,isDarkMode: Boolean) {

    var description by rememberSaveable { mutableStateOf(gender.moreAboutGender) }

    BoxWithConstraints {
        val height = maxHeight
        Column(
            modifier = Modifier
                .then(if (height < 550.dp) Modifier.fillMaxHeight(0.9f) else Modifier).background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 30.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = stringResource(Res.string.tell_something_missing),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff101828)
            )

            Spacer(modifier = Modifier.height(16.dp))

            DescriptionTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 20) {
                        description = it
                    }
                },
                label = stringResource(Res.string.describe_here),
                height = 90.dp,
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${description.length}/20",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(modifier = Modifier.height(43.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(if(!shouldDisable) 0xffF33358 else 0xffEAECF0), RoundedCornerShape(40))
                    .noRippleClickable {
                        updateGender(gender.copy(moreAboutGender = description))
                        onSave()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.save_and_close),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(if (!shouldDisable) 0xffffffff else 0xff98A2B3),
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp
                )

            }


        }
    }



}

@Composable
fun SelectGenderContent(shouldDisable: Boolean,gender: EditProfileScreenState.Gender,updateGender: (EditProfileScreenState.Gender)->Unit,onSave: () -> Unit,onAddMore:()->Unit,isDarkMode: Boolean) {
    val genders = stringArrayResource(Res.array.genders)

    var selectedIndex by remember { mutableIntStateOf(gender.selectedIndex) }
    var isVisible by remember { mutableStateOf(gender.isVisible) }
    val isButtonEnabled by remember {
        mutableStateOf(!shouldDisable)
    }
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
            imageVector = vectorResource(Res.drawable.ic_person),
            contentDescription = null,
            modifier = Modifier.width(26.93.dp).height(24.4.dp).align(Alignment.CenterHorizontally)
        )


        Spacer(Modifier.height(20.dp))

        Text(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.gender),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 27.sp,
            textAlign = TextAlign.Center,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )

        Spacer(Modifier.height(28.dp))

        genders.forEachIndexed { index, value ->
            TextWithCheckbox(
                modifier = Modifier.align(Alignment.Start),
                text = value,
                isChecked = selectedIndex == index,
                hideCheckBox = index != selectedIndex,
                onClick = {
                    selectedIndex = index
                },
                isDarkMode = isDarkMode
            )
            if (index == genders.lastIndex && selectedIndex == genders.lastIndex){
                Spacer(Modifier.height(6.dp))
                AddMoreAboutGender(
                    onClick = onAddMore,
                    text = gender.moreAboutGender.ifBlank { stringResource(Res.string.add_more_about_your_gender) }
                )

            }
            Spacer(Modifier.height(8.dp))
            CustomDivider(isDarkMode = isDarkMode)

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.pick_gender_that_describe_you),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            lineHeight = 21.sp,
            color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054)
        )

        Spacer(Modifier.height(10.dp))

        CheckboxAndText(
            isChecked = true,
            text = stringResource(Res.string.aware_gender_update),
            onClick = {},
            spacing = 2.49.dp,
            alignment = Alignment.Top,
            isDarkMode = isDarkMode
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
                    updateGender(gender.copy(selectedIndex = selectedIndex, isVisible = isVisible))
                    onSave()
                }
            }
        )

        Spacer(Modifier.height(14.dp))

    }
}

@Composable
fun UpdateMoreAboutYourGender(shouldDisable: Boolean,navigateToAddMore:()->Unit,onSave: () -> Unit,gender: EditProfileScreenState.Gender,updateGender: (EditProfileScreenState.Gender)->Unit,isDarkMode: Boolean) {

    val moreOptions = stringArrayResource(Res.array.more_gender_options)

    var selectedIndex by rememberSaveable { mutableStateOf(moreOptions.indexOfFirst { it == gender.moreAboutGender }) }

    Column(
        modifier = Modifier
            .background(if (isDarkMode) BASE_DARK else Color.White)
            .fillMaxWidth()
            .padding(
                horizontal = 17.dp
            )
            .padding(top = 34.dp, bottom = 26.49.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(Res.string.add_more_about_your_gender),
            fontFamily = PoppinsFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            letterSpacing = 0.2.sp
        )

        Spacer(Modifier.height(22.dp))

        moreOptions.forEachIndexed { index, value ->
            TextWithCheckbox(
                modifier = Modifier.align(Alignment.Start),
                text = value,
                isChecked = index == selectedIndex,
                hideCheckBox = false,
                onClick = {
                    selectedIndex = if (index == selectedIndex) -1 else index
                },
                isDarkMode = isDarkMode
            )
            Spacer(Modifier.height(8.dp))
            CustomDivider(isDarkMode = isDarkMode)
        }

        Spacer(Modifier.height(12.dp))

        TellUsMoreComponent(onClick = navigateToAddMore,isDarkMode = isDarkMode)

        Spacer(Modifier.height(29.dp))

        ButtonWithText(
            text = stringResource(Res.string.save_and_close),
            bgColor = if (selectedIndex >= 0 && !shouldDisable) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
            textColor = if (selectedIndex >= 0 && !shouldDisable) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
            onClick = {
                updateGender(gender.copy(moreAboutGender = moreOptions.getOrNull(selectedIndex) ?: ""))
                onSave()
            }
        )


    }
}