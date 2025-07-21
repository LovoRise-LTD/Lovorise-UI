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
import coinui.composeapp.generated.resources.ic_person
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.who_would_you_like_to_meet
import coinui.composeapp.generated.resources.you_will_only_be_shown_to_people_looking_to_date_your_gender
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.TextWithCheckbox
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
fun UpdateShowMeBottomSheetContent(
    data: EditProfileScreenState.ShowMe,
    onSave: (List<String>) -> Unit,
    updateShowMe: (EditProfileScreenState.ShowMe) -> Unit,
    isDarkMode: Boolean,
    showIsVisible:Boolean = true,
    buttonText:String = stringResource(Res.string.save)
) {


    var selectedItems by remember { mutableStateOf(data.selectedItems) }

    var isVisible by remember { mutableStateOf(data.isVisible) }

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
                imageVector = vectorResource(Res.drawable.ic_person),
                contentDescription = null,
                modifier = Modifier.width(26.93.dp).height(24.4.dp)
                    .align(Alignment.CenterHorizontally)
            )


            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.who_would_you_like_to_meet),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(28.dp))

            data.categories.forEach { value ->
                val isSelected = selectedItems.contains(value)
                TextWithCheckbox(
                    modifier = Modifier.align(Alignment.Start),
                    text = value,
                    isChecked = isSelected,
                    hideCheckBox = !isSelected,
                    onClick = {
                        selectedItems = if (isSelected && selectedItems.size > 1) {
                            selectedItems.toMutableList().apply {
                                remove(value)
                            }
                        }else{
                            selectedItems.toMutableList().apply {
                                if (value == "All"){
                                    removeAll(selectedItems)
                                    add(value)
                                }else{
                                    remove("All")
                                    if (selectedItems.size < 2){
                                        add(value)
                                    }
                                }
                            }
                        }

//                        if (isSelected) {
//                            selectedItems = selectedItems.toMutableList().apply {
//                                remove(value)
//                            }
//                        }else{
//                            if (selectedItems.size < 2){
//                                selectedItems = selectedItems.toMutableList().apply {
//                                    if (value == "All"){
//                                        removeAll(selectedItems)
//                                        add(value)
//                                    }else{
//                                        remove("All")
//                                        add(value)
//                                    }
//                                }
//                            }
//                        }
                    },
                    isDarkMode = isDarkMode
                )
                Spacer(Modifier.height(8.dp))
                CustomDivider(isDarkMode = isDarkMode)

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.you_will_only_be_shown_to_people_looking_to_date_your_gender),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff344054)
            )


            Spacer(modifier = Modifier.height(16.dp))

            if (showIsVisible) {
                CheckboxAndText(
                    isChecked = isVisible,
                    onClick = {
                        isVisible = !isVisible
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            ButtonWithText(
                text = buttonText,
                bgColor = if (selectedItems.isNotEmpty()) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (selectedItems.isNotEmpty()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (selectedItems.isNotEmpty()) {
                        updateShowMe(data.copy(selectedItems = selectedItems, isVisible = isVisible))
                        onSave(selectedItems)
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