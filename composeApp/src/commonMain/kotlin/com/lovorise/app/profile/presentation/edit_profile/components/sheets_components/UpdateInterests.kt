package com.lovorise.app.profile.presentation.edit_profile.components.sheets_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coinui.composeapp.generated.resources.choose_your_interests
import coinui.composeapp.generated.resources.get_better_recommendation_by_selecting_more_interests
import coinui.composeapp.generated.resources.no_result_for_interest
import coinui.composeapp.generated.resources.please_select_three_interest_to_save
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.search_interest
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.choose_interests.ChooseInterestsScreenModel
import com.lovorise.app.accounts.presentation.signup.choose_interests.components.InterestSection
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.components.Toast
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.edit_profile.components.AlwaysVisibleOnProfile
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateInterestsBottomSheetContent(
    selectedItems:List<String>,
    screenModel: ChooseInterestsScreenModel,
    isDarkMode:Boolean,
    onSave:(List<String>)->Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var toastMessage by remember { mutableStateOf("") }



    val state by screenModel.state.collectAsState()

    LaunchedEffect(true){
        screenModel.reloadItems(selectedItems)
    }
    val message = stringResource(Res.string.please_select_three_interest_to_save)

//    LaunchedEffect(state.selectedItems.size){
//        println("the current state is ${state.selectedItems.size} ${selectedItems.size} ${selectedItems.size != state.selectedItems.size} ")
//
//        if (selectedItems.isNotEmpty() && state.selectedItems.isNotEmpty())
//
//        if (state.selectedItems.size < 3 && selectedItems.size != state.selectedItems.size){
//            toastMessage = message
//        }
//    }





    Column(
        modifier = Modifier.fillMaxHeight(0.9f).noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ) {
        Box(Modifier.weight(1f)) {
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

                Text(
                    text = stringResource(Res.string.choose_your_interests),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 27.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(Res.string.get_better_recommendation_by_selecting_more_interests),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else  Color(0xff475467)
                )

                Spacer(Modifier.height(8.dp))

                CustomDivider(isDarkMode = isDarkMode)

                Spacer(Modifier.height(16.dp))

                SearchTextField(
                    query = state.searchQuery,
                    onQueryChange = screenModel::search,
                    label = stringResource(Res.string.search_interest),
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                if ((state.searchResults.isEmpty() && state.searchQuery.isNotBlank())) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(Res.string.no_result_for_interest),
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            letterSpacing = 0.2.sp,
                            lineHeight = 21.sp,
                            color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.searchResults.ifEmpty { state.allItems }) { item ->
                            key(state.selectedItems) {
                                InterestSection(
                                    item = item,
                                    onChipClicked = { screenModel.onChipClicked(it) },
                                    isSelected = { screenModel.isItemSelected(it) },
                                    onShowLess = {screenModel.onShowLess(item.id)},
                                    onShowMore = {screenModel.onShowMore(item.id)},
                                    showLess = !state.showMoreIds.contains(item.id),
                                    isDarkMode = isDarkMode
                                )
                            }
                        }
                    }
                }


                Spacer(Modifier.height(16.dp))


                AlwaysVisibleOnProfile(isDarkMode = isDarkMode)

                Spacer(Modifier.height(16.dp))

                ButtonWithText(
                    text = "${stringResource(Res.string.save)} ${state.selectedItems.size}/10",
                    bgColor = if (state.selectedItems.size >= 3) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (state.selectedItems.size >= 3) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (state.selectedItems.size >= 3) {
                            onSave(state.selectedItems)
                        } else {
                            toastMessage = message
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))


            }

            Column {
                AnimatedVisibility(
                    modifier = Modifier.padding(top = 56.dp).padding(horizontal = 30.dp)
                        .height(36.dp),
                    visible = state.selectedItems.size < 3 && toastMessage.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    Toast(text = toastMessage)

                    LaunchedEffect(Unit) {
                        delay(2000) // Hide after 2 seconds
                        toastMessage = ""
                    }

                }
            }
        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )



    }


}