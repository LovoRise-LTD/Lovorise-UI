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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.age
import coinui.composeapp.generated.resources.aware_age_update
import coinui.composeapp.generated.resources.ic_edit
import coinui.composeapp.generated.resources.months
import coinui.composeapp.generated.resources.save
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.age.ValidateAgeResult
import com.lovorise.app.accounts.presentation.signup.age.components.DobInputField
import com.lovorise.app.accounts.presentation.signup.age.validateAge
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.noRippleClickable
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
fun UpdateAgeSheetContent(
    age: EditProfileScreenState.Age,
    updateAge: (EditProfileScreenState.Age,Boolean) -> Unit,
    onSave:(Boolean)->Unit,
    isDarkMode: Boolean,
    shouldDisable:Boolean,
    isAgeUpdatable:Boolean
   // updateVisibility:()->Unit = {},
) {

    var isButtonEnabled by remember{
        mutableStateOf(false)
    }

    var isVisible by remember{ mutableStateOf(age.isVisible) }

    var validateAgeResult by remember { mutableStateOf(ValidateAgeResult()) }

    var dobText by rememberSaveable { mutableStateOf(age.dobText) }

    var isAgreementChecked by remember { mutableStateOf(age.isAgreementChecked) }

    val monthsName = stringArrayResource(Res.array.months)

    LaunchedEffect(true){
        println("the dob text is $dobText")
    }

    LaunchedEffect(key1 = dobText, key2 = isVisible, key3 = isAgreementChecked){
        if (dobText.length == 8){
            validateAgeResult = validateAge(dobText,monthsName) ?: ValidateAgeResult()
            isButtonEnabled = isAgreementChecked && validateAgeResult.age >= 18 && validateAgeResult.formatted.isNotBlank() && !shouldDisable
        }else{
            isButtonEnabled = false
        }
    }

    LaunchedEffect(isVisible){
        if (age.isVisible != isVisible && ((dobText.isNotBlank() && validateAgeResult.formatted.isNotBlank()) || dobText.isBlank())) {
            isButtonEnabled = isAgreementChecked && age.isVisible != isVisible
        }
    }


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    BoxWithConstraints {
        Column(
            modifier = Modifier.then(
                if (maxHeight < 550.dp) Modifier.fillMaxHeight(0.9f) else Modifier
            ).noRippleClickable {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ) {
            Column(
                modifier = Modifier
                    .background(if(isDarkMode) BASE_DARK else Color.White)
                    .verticalScroll(rememberScrollState())
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
                    imageVector = vectorResource(Res.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp).align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "${stringResource(Res.string.age)}, ${age.age}",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 27.sp,
                    textAlign = TextAlign.Center,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(start = 3.dp),
                    text = age.formattedAge,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff414C5E),
                    letterSpacing = 0.2.sp
                )

                Spacer(Modifier.height(8.dp))

                CheckboxAndText(
                    isChecked = isAgreementChecked,
                    text = stringResource(Res.string.aware_age_update),
                    onClick = {
                        isAgreementChecked = !isAgreementChecked
                    },
                    spacing = 2.49.dp,
                    alignment = Alignment.Top,
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(20.dp))

                DobInputField(
                    dobText = dobText,
                    onDobModified = { value, flag ->
                        if (flag) {
                            dobText = value
                        }
                    },
                    isDarkMode = isDarkMode
                )


                Spacer(Modifier.height(20.dp))



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
                            println("update the age $age ")
                            if (validateAgeResult.age != 0 && isAgeUpdatable) {
                                println("update the age1 $age")
                                updateAge(
                                    age.copy(
                                        formattedAge = validateAgeResult.formatted,
                                        age = validateAgeResult.age,
                                        dobText = validateAgeResult.dobText.ifBlank { "${validateAgeResult.day}${validateAgeResult.month}${validateAgeResult.year}" }
                                    ), false
                                )

                            } else{
                                println("update the age2 $age ")

                                if (age.isVisible != isVisible){
                                    println("update the age3 $age ")
                                    updateAge(age.copy(isVisible = isVisible),true)
                                }
                            }

                            onSave(age.isVisible != isVisible)
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