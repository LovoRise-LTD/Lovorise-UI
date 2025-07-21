package com.lovorise.app.settings.presentation.screens.delete_account

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.TextStyle
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
import coinui.composeapp.generated.resources.continue_txt
import coinui.composeapp.generated.resources.delete_account
import coinui.composeapp.generated.resources.delete_account_reasons
import coinui.composeapp.generated.resources.describe_here
import coinui.composeapp.generated.resources.other_reason
import coinui.composeapp.generated.resources.tell_us_why_are_you_leaving
import coinui.composeapp.generated.resources.we_are_sorry_see_you_go
import coinui.composeapp.generated.resources.why_are_you_leaving_lovorise
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsApiCallState
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.components.TextWithCheckbox
import com.lovorise.app.onboarding_info.DescriptionTextField
import com.lovorise.app.settings.presentation.components.TextWithChevronRight
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.DISABLED_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.DISABLED_TEXT_DARK
import com.lovorise.app.ui.DISABLED_TEXT_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

class DeleteFlow1Screen(private val accountsViewModel: AccountsViewModel) : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())

        val accountState by accountsViewModel.state.collectAsState()
        val ctx = LocalPlatformContext.current

        LaunchedEffect(true){
            accountsViewModel.resetSuccessState()
            if (accountState.user == null){
                accountsViewModel.getUser(ctx){}
            }

        }

        DeleteFlow1ScreenContent(
            isDarkMode = isDarkMode,
            navigateToDeleteFlow2 = {
                navigator.push(DeleteFlow2Screen(accountsViewModel))
            },
            goBack = {
                navigator.pop()
            },
            accountsViewModel = accountsViewModel,
            accountsState = accountState
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteFlow1ScreenContent(isDarkMode:Boolean,goBack:()->Unit,navigateToDeleteFlow2:()->Unit,accountsViewModel: AccountsViewModel,accountsState:AccountsApiCallState) {


    var isEnabled by remember { mutableStateOf(false) }

    var otherReason by remember { mutableStateOf("") }

    var selectedItems by remember{ mutableStateOf(emptyList<String>())}

    LaunchedEffect(key1 = otherReason,key2=selectedItems){
        isEnabled = otherReason.isNotBlank() || selectedItems.isNotEmpty()
    }
    val deleteReasons = stringArrayResource(Res.array.delete_account_reasons)

    val allItems by remember { mutableStateOf(deleteReasons) }


    var showOtherReasonBottomSheet by remember{ mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalPlatformContext.current

//    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accountsState.success){

        if (accountsState.success){
            println("navigating to next screen")
            navigateToDeleteFlow2()
        }
    }

    Column{

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        ConnectivityToast()


        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxSize()
                .weight(1f),
            //  horizontalAlignment = Alignment.CenterHorizontally
        ) {


            HeaderWithTitleAndBack(title = stringResource(Res.string.delete_account), onBack = goBack, isDarkMode = isDarkMode)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)

            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.why_are_you_leaving_lovorise),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )


                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Start),
                    text = stringResource(Res.string.we_are_sorry_see_you_go),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Start,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467)
                )


                Spacer(Modifier.height(16.dp))


                allItems.forEach {item ->
                    Spacer(Modifier.height(16.dp))
                    TextWithCheckbox(
                        horizontalPadding = 0.dp,
                        height = 21.dp,
                        modifier = Modifier.align(Alignment.Start),
                        text = item,
                        isChecked = selectedItems.contains(item),
                        hideCheckBox = false,
                        onClick = { selectedItems = selectedItems.toMutableList().apply {
                            if (selectedItems.contains(item)){
                                remove(item)
                            }else {
                                add(item)
                            }
                        } },
                        isDarkMode = isDarkMode
                    )
                    Spacer(Modifier.height(16.dp))
                    CustomDivider(isDarkMode = isDarkMode)
                }

                Spacer(Modifier.height(16.dp))
                TextWithChevronRight(
                    text = stringResource(Res.string.other_reason),
                    onClick = {
                        showOtherReasonBottomSheet = true
                    },
                    isDarkMode = isDarkMode
                )
                Spacer(Modifier.height(16.dp))
                CustomDivider(isDarkMode = isDarkMode)



                Spacer(Modifier.height(38.dp))

                ButtonWithText(
                    text = stringResource(Res.string.continue_txt),
                    bgColor = if (isEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isEnabled) {
                            accountsViewModel.deleteAccount(context,selectedItems.ifEmpty {listOf(otherReason)})
                        }
                    }
                )

            }




        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }

    if (showOtherReasonBottomSheet){
        ModalBottomSheet(
            containerColor = if (isDarkMode) BASE_DARK else Color.White,
            contentWindowInsets = { WindowInsets(0.dp,0.dp,0.dp,0.dp) },
            dragHandle = null,
            sheetState = sheetState,
            onDismissRequest = {
                showOtherReasonBottomSheet = false
            }
        ){
            OtherReasonBottomSheetContent(
                isDarkMode = isDarkMode,
                description = otherReason,
                onDescriptionChange = {
                    if (it.length <= 200){
                        otherReason = it
                    }
                },
                onContinue = {
                    accountsViewModel.deleteAccount(context,selectedItems.ifEmpty { listOf(otherReason)})
                }
            )
        }
    }

}


@Composable
fun OtherReasonBottomSheetContent(isDarkMode: Boolean,description:String,onDescriptionChange:(String)->Unit,onContinue:()->Unit) {

    var isButtonEnabled by remember { mutableStateOf(description.length >= 2) }

    LaunchedEffect(description){
        isButtonEnabled = description.length in 2..200
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

                Spacer(Modifier.height(16.dp))


                Text(
                    text = stringResource(Res.string.tell_us_why_are_you_leaving),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.2.sp,
                    lineHeight = 21.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828)
                )

                Spacer(Modifier.height(16.dp))


                DescriptionTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = stringResource(Res.string.describe_here),
                    height = if(height < 550.dp) 140.dp else 168.dp,
                    textStyle = TextStyle(
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        color = if (isDarkMode) Color.White else Color(0xff101828)
                    ),
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    bgColor = if (isDarkMode) BASE_DARK else Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${description.length}/200",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.2.sp
                )


                Spacer(modifier = Modifier.height(46.dp))

                ButtonWithText(
                    text = stringResource(Res.string.continue_txt),
                    bgColor = if (isButtonEnabled) PRIMARY else if (isDarkMode) DISABLED_DARK else DISABLED_LIGHT,
                    textColor = if (isButtonEnabled) Color.White else if (isDarkMode) DISABLED_TEXT_DARK else DISABLED_TEXT_LIGHT,
                    onClick = {
                        if (isButtonEnabled) {
                            onContinue()
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