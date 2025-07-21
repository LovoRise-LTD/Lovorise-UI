package com.lovorise.app.profile.presentation.edit_profile.components.sheets_components

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
import coinui.composeapp.generated.resources.no_result_for_language
import coinui.composeapp.generated.resources.save
import coinui.composeapp.generated.resources.search_languages
import coinui.composeapp.generated.resources.select_up_to_five
import coinui.composeapp.generated.resources.what_languages_do_you_know
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.signup.language.LanguageScreenModel
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.profile.presentation.edit_profile.EditProfileScreenState
import com.lovorise.app.profile.presentation.edit_profile.components.CheckboxAndText
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateLanguagesSheetContent(
    languages: EditProfileScreenState.Languages,
    screenModel: LanguageScreenModel,
    isDarkMode:Boolean,
    onSave:(List<String>)->Unit,
    updateLanguage:(EditProfileScreenState.Languages)->Unit,
    titleText:String = stringResource(Res.string.what_languages_do_you_know),
    buttonText:String = stringResource(Res.string.save)
) {


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    val state by screenModel.state.collectAsState()

    LaunchedEffect(true){
        screenModel.reloadItems(languages.languages)
    }

    Column(
        modifier = Modifier.fillMaxHeight(0.9f).noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ) {

        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .weight(1f)
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
                text = titleText,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 27.sp,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.select_up_to_five),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 21.sp,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )


            Spacer(Modifier.height(16.dp))

            SearchTextField(
                query = state.searchQuery,
                onQueryChange = screenModel::search,
                label = stringResource(Res.string.search_languages),
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(16.dp))

            if ((state.searchResults.isEmpty() && state.searchQuery.isNotBlank())){
                Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){
                    Text(
                        text = stringResource(Res.string.no_result_for_language),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = 0.2.sp,
                        lineHeight = 21.sp,
                        color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                        textAlign = TextAlign.Center
                    )
                }
            }else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.searchResults.ifEmpty { state.allLanguages }) {item ->
                        TextWithCheckbox(
                            modifier = Modifier.align(Alignment.Start),
                            text = item,
                            isChecked = screenModel.isItemSelected(item),
                            hideCheckBox = false,
                            onClick = { screenModel.onItemClicked(item) },
                            isDarkMode = isDarkMode
                        )
                        Spacer(Modifier.height(8.dp))
                        CustomDivider(isDarkMode = isDarkMode)
                    }
                }
            }


            Spacer(Modifier.height(13.dp))


            if (buttonText == stringResource(Res.string.save)) {
                CheckboxAndText(
                    isChecked = languages.isVisible,
                    onClick = {
                        updateLanguage(languages.copy(isVisible = !languages.isVisible))
                    },
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))
            }

            ButtonWithText(
                text = "$buttonText ${state.selectedLanguages.size}/5",
                bgColor = if (state.selectedLanguages.isNotEmpty()) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                textColor = if (state.selectedLanguages.isNotEmpty()) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                onClick = {
                    if (state.selectedLanguages.isNotEmpty()) {
                        updateLanguage(languages.copy(languages = state.selectedLanguages))
                        onSave(state.selectedLanguages)
                    }
                }
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