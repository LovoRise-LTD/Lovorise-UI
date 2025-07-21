package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.languages
import coinui.composeapp.generated.resources.*
import coinui.composeapp.generated.resources.no_result_for_language
import coinui.composeapp.generated.resources.search
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.accounts.presentation.signup.email.CircularLoader
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.SearchTextField
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.TextWithBackground
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

class AppLanguageScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val context = LocalPlatformContext.current

        LaunchedEffect(true) {

            if (accountsState.language.isNullOrBlank()) {
                accountsViewModel.getAppLanguage(context)
            }
        }

        AppLanguageScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            accountsState = accountsState,
            accountsViewModel = accountsViewModel
        )

    }
}

@Composable
fun AppLanguageScreenContent(isDarkMode:Boolean,goBack:()->Unit,accountsState: AccountsApiCallState,accountsViewModel: AccountsViewModel) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val languagesRes = stringArrayResource(Res.array.languages)

    var query by remember { mutableStateOf("") }

    var searchResults by remember { mutableStateOf(emptyList<String>()) }

    val languages by remember { mutableStateOf(languagesRes) }

    var selectedLanguage by remember { mutableStateOf(accountsState.language ?: languagesRes[0]) }

    val context = LocalPlatformContext.current

    LaunchedEffect(accountsState.language){
        if (!accountsState.language.isNullOrBlank()){
            selectedLanguage = accountsState.language
        }
    }

    LaunchedEffect(query){
        if (query.isNotBlank()){
            // delay(100L)
            val filteredItems = languages.filter { item ->
                item.lowercase().contains(query.lowercase())
            }
            searchResults = filteredItems
        }else{
            searchResults = emptyList()
        }
    }

    Column(
        modifier = Modifier.noRippleClickable {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    ) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
        ) {


            HeaderWithTitleAndBack(title = stringResource(Res.string.language), onBack = goBack, isDarkMode = isDarkMode)

            TextWithBackground(
                text = stringResource(Res.string.using_default_language_message),
                verticalPadding = 10.dp,
                textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)
            )


            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(16.dp))

                SearchTextField(
                    query = query,
                    onQueryChange = { query = it },
                    label = stringResource(Res.string.search),
                    isDarkMode = isDarkMode
                )

                Spacer(Modifier.height(16.dp))

                if ((searchResults.isEmpty() && query.isNotBlank())) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
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
                } else {

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(searchResults.ifEmpty { languages }) { item ->
                            TextWithCheckbox(
                                modifier = Modifier.align(Alignment.Start),
                                text = item,
                                isChecked = item == selectedLanguage,
                                hideCheckBox = false,
                                onClick = {
                                    if (selectedLanguage != item){
                                        selectedLanguage = item
                                        accountsViewModel.setAppLanguage(context,selectedLanguage)
                                    }
                                },
                                isDarkMode = isDarkMode
                            )
                            Spacer(Modifier.height(8.dp))
                            CustomDivider(isDarkMode = isDarkMode)
                        }
                    }
                }


                Spacer(Modifier.height(16.dp))
            }





        }



        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )



    }
    if (accountsState.isLoading){
        CircularLoader(true)
    }

}



