package com.lovorise.app.settings.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.LocalPlatformContext
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.active_sessions
import coinui.composeapp.generated.resources.automatically_terminate_old_sessions
import coinui.composeapp.generated.resources.cancel
import coinui.composeapp.generated.resources.ic_android_device
import coinui.composeapp.generated.resources.ic_apple_device
import coinui.composeapp.generated.resources.ic_checked_session
import coinui.composeapp.generated.resources.ic_device_location
import coinui.composeapp.generated.resources.ic_phone_frame
import coinui.composeapp.generated.resources.ic_terminate_sessions
import coinui.composeapp.generated.resources.ic_unchecked_session
import coinui.composeapp.generated.resources.if_inactive_for
import coinui.composeapp.generated.resources.manage_device
import coinui.composeapp.generated.resources.no_other_active_sessions
import coinui.composeapp.generated.resources.self_destruct_options
import coinui.composeapp.generated.resources.session_self_destruct
import coinui.composeapp.generated.resources.sign_out_all_devices_expect_this_one
import coinui.composeapp.generated.resources.terminate_all_other_sessions
import coinui.composeapp.generated.resources.terminate_session
import coinui.composeapp.generated.resources.the_lovorise_app_is_available_for_android_and_ios
import coinui.composeapp.generated.resources.this_device
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ButtonWithText
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.components.HeaderWithTitleAndBack
import com.lovorise.app.getAppVersion
import com.lovorise.app.getDeviceName
import com.lovorise.app.isAndroid
import com.lovorise.app.noRippleClickable
import com.lovorise.app.settings.presentation.components.TextWithBackground
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.PRIMARY
import com.lovorise.app.ui.ThemeViewModel
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


class ManageDeviceScreen : Screen{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val accountsViewModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val accountsState by accountsViewModel.state.collectAsState()
        val context = LocalPlatformContext.current

        val currentDeviceInfo = DeviceInfo(
            type = if (isAndroid()) DeviceInfo.DeviceType.ANDROID else DeviceInfo.DeviceType.IOS,
            lastActive = "Online",
            name = getDeviceName(),
            location = "${accountsState.currentLocation?.city}, ${accountsState.currentLocation?.country}",
            version = getAppVersion(context)
        )

        ManageDeviceScreenContent(
            isDarkMode = isDarkMode,
            goBack = {
                navigator.pop()
            },
            currentDeviceInfo = currentDeviceInfo
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageDeviceScreenContent(isDarkMode: Boolean, goBack:()-> Unit,currentDeviceInfo: DeviceInfo){

    val selfDestructOptions = stringArrayResource(Res.array.self_destruct_options)
    var selectedSelfDestructOptionIndex by remember { mutableStateOf(1)}
    var showSelfDestructOptions by remember { mutableStateOf(false)}
    var selectedDeviceInfo by remember { mutableStateOf<DeviceInfo?>(null)}
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var activeSessions by remember { mutableStateOf(listOf<DeviceInfo>(
        DeviceInfo(
            type = DeviceInfo.DeviceType.ANDROID,
            name = "POCO",
            location = "Dhaka, Bangladesh",
            lastActive = "1:40",
            version = "1.0.0"
        ),
        DeviceInfo(
            type = DeviceInfo.DeviceType.IOS,
            name = "iPhone 13",
            location = "Dhaka, Bangladesh",
            lastActive = "Mar 20",
            version = "1.0.0"
        )

    )) }



    Column(modifier = Modifier.background(if (isDarkMode) BASE_DARK else Color.White)) {

        Spacer(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )
        HeaderWithTitleAndBack(title = stringResource(Res.string.manage_device), onBack = goBack, isDarkMode = isDarkMode)

        Column(
            modifier = Modifier
                //   .background(Color.White)
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.Start
        ){

            Spacer(Modifier.height(23.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(Res.string.this_device),
                color = if (isDarkMode) Color.White else Color(0xff101828),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily(),
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(22.dp))

            DeviceItem(modifier = Modifier.padding(horizontal = 16.dp),info = currentDeviceInfo, isDarkMode = isDarkMode)

            Spacer(Modifier.height(13.dp))



            if (activeSessions.isEmpty()){
                ManageDeviceDivider(isDarkMode)
                Row(Modifier.height(56.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.no_other_active_sessions),
                        color = if (isDarkMode) Color.White else Color(0xff101828),
                        lineHeight = 20.sp,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }else{

                CustomDivider(isDarkMode = isDarkMode)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = vectorResource(Res.drawable.ic_terminate_sessions),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.terminate_all_other_sessions),
                        color = PRIMARY,
                        fontFamily = PoppinsFontFamily(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(21.dp))

                TextWithBackground(stringResource(Res.string.sign_out_all_devices_expect_this_one), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
                ManageDeviceDivider(isDarkMode)

                Spacer(Modifier.height(18.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    text = stringResource(Res.string.active_sessions),
                    color = if (isDarkMode) Color.White else Color(0xff101828),
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily(),
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(22.dp))

                activeSessions.forEachIndexed { index, item ->
                    DeviceItem(info = item, isDarkMode = isDarkMode, modifier = Modifier.padding(horizontal = 16.dp)){
                        selectedDeviceInfo = item
                    }
                    if (index != activeSessions.lastIndex){
                        CustomDivider(isDarkMode = isDarkMode)
                    }
                }
                Spacer(Modifier.height(16.dp))

            }



            TextWithBackground(stringResource(Res.string.the_lovorise_app_is_available_for_android_and_ios), bgColor = if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9), textColor = if (isDarkMode) DISABLED_LIGHT else Color(0xff475467))
            ManageDeviceDivider(isDarkMode)

            AutomateTerminateSection(modifier = Modifier.padding(horizontal = 16.dp),isDarkMode, deleteAfter = selfDestructOptions[selectedSelfDestructOptionIndex], onClick = {
                showSelfDestructOptions = true
            })

            Box(Modifier.fillMaxSize().weight(1f).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))


        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9))
        )

        

    }

    if (showSelfDestructOptions){
        SessionSelfDestructOptionDialog(items = selfDestructOptions, selectedIndex = selectedSelfDestructOptionIndex, onCancel = { showSelfDestructOptions = false }, onItemClick = { selectedSelfDestructOptionIndex = it; showSelfDestructOptions=false })
    }

    if (selectedDeviceInfo != null) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0.dp, 0.dp, 0.dp, 0.dp) },
            //  modifier = Modifier.navigationBarsPadding(),
            sheetState = sheetState,
            onDismissRequest = {
                selectedDeviceInfo = null
            },
            shape = RoundedCornerShape(topStartPercent = 4, topEndPercent = 4),
            dragHandle = null,
        ) {
            DeviceInfoBottomSheetContent(
                isDarkMode = isDarkMode,
                onTerminateSession = {},
                info = selectedDeviceInfo!!
            )

        }
    }
}

@Composable
fun AutomateTerminateSection(modifier: Modifier,isDarkMode: Boolean,deleteAfter: String,onClick:()-> Unit){
    Column(modifier.noRippleClickable(onClick)) {
        Spacer(Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.automatically_terminate_old_sessions),
            color = if (isDarkMode) Color.White else Color(0xff101828),
            lineHeight = 20.sp,
            fontFamily = PoppinsFontFamily(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(Res.string.if_inactive_for),
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff414C5E),
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = deleteAfter,
                color = PRIMARY,
                lineHeight = 20.sp,
                fontFamily = PoppinsFontFamily(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(Modifier.height(10.dp))

    }
}


@Composable
fun ManageDeviceDivider(isDarkMode: Boolean){
    Box(Modifier.fillMaxWidth().height(16.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffF3F5F9)))
}




@Composable
fun DeviceItem(modifier: Modifier = Modifier, info: DeviceInfo, isDarkMode: Boolean,onClick:()->Unit={}){

    Row(modifier = modifier.noRippleClickable(onClick)) {
        Image(modifier = Modifier.size(24.dp), imageVector = vectorResource(if(info.type == DeviceInfo.DeviceType.ANDROID) Res.drawable.ic_android_device else Res.drawable.ic_apple_device),contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = info.name,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) Color.White else Color(0xff101828),
                lineHeight = 20.sp,
                fontSize = 14.sp
            )
            Text(
                text = if (info.type == DeviceInfo.DeviceType.ANDROID) "Lovorise Android ${info.version}" else "Lovorise iOS ${info.version}",
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                color = if (isDarkMode) DISABLED_LIGHT else Color(0xff414C5E),
                lineHeight = 20.sp,
                fontSize = 14.sp
            )
            Row(modifier = Modifier.fillMaxWidth().height(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = info.location,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3),
                    lineHeight = 20.sp,
                    fontSize = 12.sp
                )

                Box(Modifier.padding(horizontal = 4.dp).size(3.dp).background(if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3),
                    CircleShape
                ))

                Text(
                    text = info.lastActive,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff414C5E),
                    lineHeight = 20.sp,
                    fontSize = 12.sp
                )

            }


        }

    }
}

@Composable
fun SessionSelfDestructOptionDialog(items: List<String>,selectedIndex: Int,onCancel:()-> Unit,onItemClick:(Int)-> Unit) {

    Dialog(
        onDismissRequest = { onCancel() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .background(
                     Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {

            Column(Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.session_self_destruct),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xff101828)
                )
                Spacer(Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    Row(Modifier.fillMaxWidth().height(24.dp).noRippleClickable{onItemClick(index)}, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            imageVector = vectorResource(if (selectedIndex == index) Res.drawable.ic_checked_session else Res.drawable.ic_unchecked_session),
                            contentDescription = null,
                          //  modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = item,
                            fontFamily = PoppinsFontFamily(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xff414C5E)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(Modifier.height(8.dp))

                Row (Modifier.fillMaxWidth()){
                    Spacer(Modifier.weight(1f))

                    Text(
                        modifier = Modifier.noRippleClickable(onCancel),
                        text = stringResource(Res.string.cancel),
                        fontFamily = PoppinsFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = PRIMARY
                    )
                }

            }


        }
    }
}


@Composable
fun DeviceInfoBottomSheetContent(isDarkMode:Boolean,onTerminateSession:()->Unit,info: DeviceInfo) {
    Column {
        Column(
            modifier = Modifier
                .background(if (isDarkMode) BASE_DARK else Color.White)
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(16.dp))

            Image(modifier = Modifier.size(48.dp), imageVector = vectorResource(if(info.type == DeviceInfo.DeviceType.ANDROID) Res.drawable.ic_android_device else Res.drawable.ic_apple_device),contentDescription = null)
            Spacer(Modifier.width(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = info.name,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = if (isDarkMode) Color.White else Color(0xff101828)
            )

            Spacer(Modifier.height(2.dp))

            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                text = info.lastActive,
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.2.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff98A2B3)
            )

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth().height(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_phone_frame),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (info.type == DeviceInfo.DeviceType.ANDROID) "Lovorise Android ${info.version}" else "Lovorise iOS ${info.version}",
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff414C5E),
                    lineHeight = 20.sp,
                    fontSize = 14.sp
                )

            }

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth().height(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = vectorResource(Res.drawable.ic_device_location),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = info.location,
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff98A2B3),
                    lineHeight = 20.sp,
                    fontSize = 14.sp
                )

            }

            Spacer(Modifier.height(16.dp))

            ButtonWithText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.terminate_session),
                bgColor = Color(0xffF33358),
                textColor = Color(0xffffffff),
                onClick = onTerminateSession
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


data class DeviceInfo(
    val type: DeviceType,
    val name: String,
    val location: String,
    val lastActive: String,
    val version: String
){
    enum class DeviceType{
        ANDROID,IOS
    }
}
