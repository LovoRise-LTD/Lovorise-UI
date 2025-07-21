package com.lovorise.app.profile.presentation.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.by_continuing_you_agree_to_our
import coinui.composeapp.generated.resources.choose_how_your_want_to_verify
import coinui.composeapp.generated.resources.continue_txt
import coinui.composeapp.generated.resources.country_of_issued
import coinui.composeapp.generated.resources.ic_driving_license
import coinui.composeapp.generated.resources.ic_driving_license_enabled
import coinui.composeapp.generated.resources.ic_drop_down
import coinui.composeapp.generated.resources.ic_government_issued_id
import coinui.composeapp.generated.resources.ic_government_issued_id_enabled
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.ic_passport
import coinui.composeapp.generated.resources.ic_passport_enabled
import coinui.composeapp.generated.resources.ic_xmark
import coinui.composeapp.generated.resources.id_verification_types
import coinui.composeapp.generated.resources.identity_verification
import coinui.composeapp.generated.resources.only_mentioned_docs_accepted
import coinui.composeapp.generated.resources.privacy_policy
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.noRippleClickable
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class ChooseIDVerificationOptionScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val screenModel = navigator.koinNavigatorScreenModel<IDProfileVerificationScreenModel>()
        val state by screenModel.state.collectAsState()

        ChooseIDVerificationOptionScreenContent(
            isDarkMode = isDarkMode,
            state = state,
            screenModel = screenModel,
            onBack = { navigator.pop() },
            navigateToIDVerificationScreen = {
                navigator.push(IdentityVerificationScreen())
            }
        )
    }
}

@Composable
fun ChooseIDVerificationOptionScreenContent(isDarkMode:Boolean,onBack:()->Unit,state: IDProfileVerificationScreenState,screenModel: IDProfileVerificationScreenModel,navigateToIDVerificationScreen:()->Unit) {

    val idVerificationTypes = stringArrayResource(Res.array.id_verification_types)
    val context = LocalPlatformContext.current

    Column(Modifier.fillMaxSize()
        .background(if (isDarkMode) BASE_DARK else Color.White)) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Column(Modifier.fillMaxSize().weight(1f).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(13.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Box(
                    Modifier.size(24.dp).noRippleClickable(onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color(0xff475467),
                        modifier = Modifier.width(18.dp).height(14.dp),
                        imageVector = vectorResource(Res.drawable.ic_left),
                        contentDescription = null
                    )
                }


                Box(
                    Modifier.size(24.dp).noRippleClickable(onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color(0xff475467),
                        imageVector = vectorResource(Res.drawable.ic_xmark),
                        contentDescription = null
                    )
                }

            }

            Spacer(Modifier.height(21.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.identity_verification),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.country_of_issued),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            
            CountryDropDown(
                text = "Bangladesh",
                onClick = {},
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.choose_how_your_want_to_verify),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.only_mentioned_docs_accepted),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
                fontFamily = PoppinsFontFamily(),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
            )

            Spacer(Modifier.height(16.dp))

            IDVerificationMethod(
                isSelected = state.selectedVerificationMethod == IDProfileVerificationScreenState.VerificationMethod.GOVERNMENT_ISSUED_ID,
                onClick = {screenModel.updateVerificationMethod(IDProfileVerificationScreenState.VerificationMethod.GOVERNMENT_ISSUED_ID)},
                selectedIcon = Res.drawable.ic_government_issued_id_enabled,
                unselectedIcon = Res.drawable.ic_government_issued_id,
                title = idVerificationTypes[0],
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(16.dp))

            IDVerificationMethod(
                isSelected = state.selectedVerificationMethod == IDProfileVerificationScreenState.VerificationMethod.DRIVING_LICENSE,
                onClick = {screenModel.updateVerificationMethod(IDProfileVerificationScreenState.VerificationMethod.DRIVING_LICENSE)},
                selectedIcon = Res.drawable.ic_driving_license_enabled,
                unselectedIcon = Res.drawable.ic_driving_license,
                title = idVerificationTypes[1],
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.height(16.dp))

            IDVerificationMethod(
                isSelected = state.selectedVerificationMethod == IDProfileVerificationScreenState.VerificationMethod.PASSPORT,
                onClick = {screenModel.updateVerificationMethod(IDProfileVerificationScreenState.VerificationMethod.PASSPORT)},
                selectedIcon = Res.drawable.ic_passport_enabled,
                unselectedIcon = Res.drawable.ic_passport,
                title = idVerificationTypes[2],
                isDarkMode = isDarkMode
            )

            Spacer(Modifier.weight(1f))




            Row(Modifier.fillMaxWidth().height(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(Res.string.by_continuing_you_agree_to_our) + " ",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff667085)
                )

                Text(
                    text = stringResource(Res.string.privacy_policy),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                    fontFamily = PoppinsFontFamily(),
                    color = Color(0xffF33358),
                    modifier = Modifier.noRippleClickable {
                        openUrlInCustomTab(AppConstants.PRIVACY_POLICY_URL, context)
                    }
                )
            }



            Spacer(Modifier.height(26.dp))

            ButtonWithText(
                text = stringResource(Res.string.continue_txt),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = navigateToIDVerificationScreen
            )

            Spacer(Modifier.height(35.dp))




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
fun IDVerificationMethod(isSelected:Boolean,selectedIcon:DrawableResource,unselectedIcon:DrawableResource,title:String,onClick:()->Unit,isDarkMode: Boolean) {

    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).noRippleClickable(onClick).background(if (isSelected) Color(0xffF33358) else if (isDarkMode) CARD_BG_DARK else DISABLED_LIGHT, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(Modifier.width(16.dp))

        Icon(
            tint = if(isSelected) Color.White else if (isDarkMode) DISABLED_LIGHT else Color(0xff344054),
            imageVector = vectorResource(if (isSelected) selectedIcon else unselectedIcon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            color = if (isSelected) Color.White else if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
        )

    }
    
}

@Composable
fun CountryDropDown(text:String,onClick: () -> Unit,isDarkMode: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .noRippleClickable(onClick)
            .border(
                width = 1.dp,
                color = Color(0xffD0D5DD),
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = text,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.2.sp,
            fontFamily = PoppinsFontFamily(),
            color = if (isDarkMode) Color.White else Color(0xff101828)
        )

        Icon(
            tint = if (isDarkMode) Color.White else Color(0xff475467),
            imageVector = vectorResource(Res.drawable.ic_drop_down),
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp).size(16.dp)
        )


    }
    
}
