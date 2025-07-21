package com.lovorise.app.lovorise_hearts.presentation.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import coinui.composeapp.generated.resources.buy_hearts
import coinui.composeapp.generated.resources.ic_heart_small
import coinui.composeapp.generated.resources.ic_history
import coinui.composeapp.generated.resources.ic_left
import coinui.composeapp.generated.resources.lovorise_hearts
import com.lovorise.app.InAppPurchase
import com.lovorise.app.PoppinsFontFamily
import com.lovorise.app.accounts.presentation.AccountsViewModel
import com.lovorise.app.components.ConnectivityToast
import com.lovorise.app.isAndroid
import com.lovorise.app.libs.iap.AppleReceiptData
import com.lovorise.app.libs.iap.GoogleReceiptData
import com.lovorise.app.libs.iap.PurchaseProductItem
import com.lovorise.app.libs.openUrlInCustomTab
import com.lovorise.app.lovorise_hearts.presentation.components.BuyLovoriseHeartsItem
import com.lovorise.app.lovorise_hearts.presentation.components.BuyLovoriseHeartsItems
import com.lovorise.app.lovorise_hearts.presentation.components.LovoriseHeartScreenWelcomeDialog
import com.lovorise.app.noRippleClickable
import com.lovorise.app.profile.presentation.ProfileScreenModel
import com.lovorise.app.profile.presentation.ProfileScreensState
import com.lovorise.app.settings.presentation.screens.openUrlExternally
import com.lovorise.app.ui.BASE_DARK
import com.lovorise.app.ui.CARD_BG_DARK
import com.lovorise.app.ui.DISABLED_LIGHT
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.AppConstants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

class LovoriseHeartBalanceScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val themeViewModel = navigator.koinNavigatorScreenModel<ThemeViewModel>()
        val isDarkMode = themeViewModel.isDarkMode(isSystemInDarkTheme())
        val profileScreenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profileScreenState by profileScreenModel.state.collectAsState()
        val accountsScreenModel = navigator.koinNavigatorScreenModel<AccountsViewModel>()
        val context = LocalPlatformContext.current

        LovoriseHeartsBalanceScreenContent(
            onBack = {
                navigator.pop()
            },
            isDarkMode = isDarkMode,
            onPurchase = { productId, hearts ->
                InAppPurchase.DEFAULT?.startTransaction(PurchaseProductItem(productId = productId, type = PurchaseProductItem.ProductType.IN_APP)){ receiptData->
                    profileScreenModel.verifyPurchase(
                        verifier = {
                            if(isAndroid()){
                                accountsScreenModel.verifyGooglePurchase(receiptData as GoogleReceiptData, context = context)
                            }else{
                                accountsScreenModel.verifyApplePurchase(receiptData as AppleReceiptData, context = context)
                            }
                        },
                        hearts = hearts
                    )
                }
            },
            profileScreenState = profileScreenState,
            profileScreenModel = profileScreenModel,
            navigateToHistory = {
                navigator.push(TransactionHistoryScreen())
            }
        )
    }
}

@Composable
fun LovoriseHeartsBalanceScreenContent(onBack:()->Unit, isDarkMode:Boolean,onPurchase:(String,Long)->Unit,profileScreenState: ProfileScreensState,profileScreenModel: ProfileScreenModel,navigateToHistory:()->Unit) {


    val context = LocalPlatformContext.current

//    LaunchedEffect(true){
//        if (!showWelcomeDialog) {
//           // delay(200)
//            showWelcomeDialog = true
//        }
//
//    }

    Column(
        modifier = Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White)
    ) {


        Spacer(
            modifier = Modifier
                .background( if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0).copy(alpha = 0.25f))
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()

        )
        ConnectivityToast()
        Column(
            modifier = Modifier.fillMaxWidth().height(137.dp).background(if (isDarkMode) CARD_BG_DARK else Color(0xffEAECF0).copy(alpha = 0.25f))
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth().height(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.padding(start = 16.dp).size(24.dp).noRippleClickable { onBack() },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        tint = if (isDarkMode) Color.White else Color.Black,
//                    modifier = Modifier.width(16.dp).height(12.dp),
                        imageVector = vectorResource(Res.drawable.ic_left),
                        contentDescription = "back"
                    )
                }
                Spacer(Modifier.weight(1f))

                Icon(
                    tint = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.padding(end = 16.dp).size(24.dp).noRippleClickable(navigateToHistory),
                    imageVector = vectorResource(Res.drawable.ic_history),
                    contentDescription = "back"
                )

            }

            Spacer(Modifier.height(12.dp))

            Box(Modifier.fillMaxWidth().height(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(Res.string.lovorise_hearts),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    color = if (isDarkMode) DISABLED_LIGHT else Color(0xff344054)
                )
            }

            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(38.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {



                Image(
                    imageVector = vectorResource(Res.drawable.ic_heart_small),
                    contentDescription = "hearts",
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.width(8.dp))


                Text(
                    text = profileScreenState.hearts.toString(),
                    fontFamily = PoppinsFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    color = if (isDarkMode) Color.White else Color(0xff101828),
//                    lineHeight = 32.sp,
                    letterSpacing = 0.2.sp,
                )



            }




        }

        Column(modifier = Modifier.fillMaxSize().background(if (isDarkMode) BASE_DARK else Color.White).weight(1f).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.buy_hearts),
                fontFamily = PoppinsFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                color = Color(0xffF33358)
            )

            Spacer(Modifier.height(16.dp))

            //coin items
            BuyLovoriseHeartsItems(
                isDarkMode = isDarkMode,
                item = BuyLovoriseHeartsItem("1'000","£ 9.99"),
                onClick = {
                    onPurchase("com.lovorise.package_1",1000)
                }
            )

            Spacer(Modifier.height(8.dp))

            BuyLovoriseHeartsItems(
                isDarkMode = isDarkMode,
                item = BuyLovoriseHeartsItem("2'500","£ 19.99"),
                onClick = {
                    onPurchase("com.lovorise.package_2",2500)
                }
            )

            Spacer(Modifier.height(8.dp))

            BuyLovoriseHeartsItems(
                isDarkMode = isDarkMode,
                item = BuyLovoriseHeartsItem("5'000","£ 29.99"),
                onClick = {
                    onPurchase("com.lovorise.package_3",5000)
                }
            )

            Spacer(Modifier.height(8.dp))

            BuyLovoriseHeartsItems(
                isDarkMode = isDarkMode,
                item = BuyLovoriseHeartsItem("10'000","£ 49.99"),
                onClick = {
                    onPurchase("com.lovorise.package_4",10000)
                }
            )

            Spacer(Modifier.height(8.dp))

            BuyLovoriseHeartsItems(
                isDarkMode = isDarkMode,
                item = BuyLovoriseHeartsItem("25'000","£ 99.99"),
                onClick = {
                    onPurchase("com.lovorise.package_5",25000)
                }
            )


        }


        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(if (isDarkMode) BASE_DARK else Color.White)
        )


    }

    if (profileScreenState.showHeartShopWelcomeDialog!!){
        LovoriseHeartScreenWelcomeDialog(
            onConfirm = {
                profileScreenModel.setHeartShopWelcomeDialogPrefs(context)
            },
            onDismiss = {
                onBack()
            },
            onMaybeLater = {
                onBack()
                //showWelcomeDialog = false
            },
            onTermsAndCondition = {
                openUrlInCustomTab(AppConstants.TERMS_AND_CONDITIONS_URL,context)
            },
            isDarkMode = isDarkMode
        )
    }



}