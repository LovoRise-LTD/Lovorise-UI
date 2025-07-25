package com.lovorise.app

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chaintech.videoplayer.util.PlaybackPreference
import coinui.composeapp.generated.resources.Res
import coinui.composeapp.generated.resources.ic_refresh_ice_breaker
import coinui.composeapp.generated.resources.pick_a_question_to_break_ice
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.lovorise.app.chat.presentation.components.MessageBox
import com.lovorise.app.components.CustomDivider
import com.lovorise.app.libs.iap.InAppPurchaseAndSubscription
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.GpsProvider
import com.lovorise.app.util.extractAndStoreReferralCodeFromUrl
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {

    private lateinit var gpsProvider: GpsProvider

    private val iap by lazy { InAppPurchaseAndSubscription(this,this, androidCBHook = {
        println("the purchase data is $it")
    }) }



    override fun onCreate(savedInstanceState: Bundle?) {


        //FirebaseApp.initializeApp(this)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleDeepLink(intent)
        PlaybackPreference.initialize(this)



        val lightTransparentStyle = SystemBarStyle.light(
            scrim = TRANSPARENT,
            darkScrim = TRANSPARENT
        )

        val darkTransparentStyle = SystemBarStyle.dark(
            scrim = TRANSPARENT,
          //  darkScrim = TRANSPARENT
        )




//        WindowCompat.setDecorFitsSystemWindows(window, false)
        gpsProvider = AndroidGpsProvider(this)



        setContent {
            val themeViewModel = koinInject<ThemeViewModel>()
            LaunchedEffect(true) {
                themeViewModel.init(this@MainActivity)
                
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    println("the fcm token for android is ${task.result}")
                })
            }
//
            val currentTheme by themeViewModel.currentThemeType.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()

            val isDark = remember(currentTheme) {  themeViewModel.isDarkMode(systemDark) }


            enableEdgeToEdge(
                statusBarStyle = if (isDark) darkTransparentStyle else lightTransparentStyle,
                navigationBarStyle = if (isDark) darkTransparentStyle else lightTransparentStyle
            )
            val view = LocalView.current
            LaunchedEffect(isDark) {
                WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = !isDark
            }


            LaunchedEffect(true) {
                disableUiKitOverscroll()
            }

            Box(modifier = Modifier.fillMaxSize()) {
//                val screenDimensions = LocalConfiguration.current
                CompositionLocalProvider(*provideNullAndroidOverscrollConfiguration()) {
//                    App(
//                        mediaPlayerWorker = null,
//                        gpsProvider = gpsProvider,
//                        iap = iap
//                    )
//                    GiftAnimation()

                    ScreenPreview()
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { extractAndStoreReferralCodeFromUrl(it.toString(),this) }
    }

}

//678
@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 390,
    //showSystemUi = true
)
@Composable
fun ScreenPreview(){
//    val screenHeight = LocalConfiguration.current.screenHeightDp
//    val screenWidth = LocalConfiguration.current.screenWidthDp
  //  App(screenHeight,screenWidth, isPreview = true,null,null)
   // var otp by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp),
//            .safeContentPadding(),
       // contentAlignment = Alignment.Center
    ){

//        FilterTransactionBottomSheetContent(
//            isDarkMode = false,
//            onApply = {
////                sheetState.hideWithCompletion(coroutineScope){
////                    purchaseScreenModel.updateTransactionFilterSheetState(false)
////                }
//            }
//        )


        //IceBreakerSection(onRefresh = {}, onItemClick = {})

        Column {
            MessageBox(
                message = "This is test message",
                timestamp = "june 11",
                isDarkMode = false,
                isSent = true,
                modifier = Modifier,
                showTip = true,
                showDoubleCheckMark = true,
                separatorText = "",
                onLongPress = {  },
                isSelected = true,
                onClick = {},
                replyData = null,
                linkPreviewData = null,
                onLinkClicked = {},
                onSwipe = {},
                medias = emptyList()
            )
            MessageBox(
                message = "This is test message 22",
                timestamp = "june 12",
                isDarkMode = false,
                isSent = true,
                modifier = Modifier,
                showTip = true,
                showDoubleCheckMark = true,
                separatorText = "",
                onLongPress = {  },
                isSelected = true,
                onClick = {},
                replyData = null,
                linkPreviewData = null,
                onLinkClicked = {},
                onSwipe = {},
                medias = emptyList()
            )

        }




//        TransactionDetailsBottomSheetContent(isDarkMode = false, onCancel = {}, transactionData = TransactionData(
//            id = "", change = 100, name = "In app purchase", timestamp = "",
//            type = TransactionData.Type.OUTGOING,
//            formattedDate  ="June 1, 2023"))

//        Row(Modifier.fillMaxWidth().height(28.2.dp)) {
//            AudioWaveform(
//                amplitudes = listOf(1,2,3,4,5,2,4,3,4,5,6,7,3,2,11,2,1,2,3,1),
//                waveformAlignment = WaveformAlignment.Center,
//                progress = 0.5f,
//                spikeWidth = 2.5.dp,
//                spikeRadius = 2.dp,
//                spikePadding = 2.dp,
//                amplitudeType = AmplitudeType.Max
//            ) {
//
//            }
//        }
//        GiftAnimation()

//       PurchaseSubscriptionScreenContent(
//         profileScreenModel = ProfileScreenModel(),
//           onPurchase = {a,b-> },
//           isDarkMode = true,
//           navigateToLikedScreen = {},
//           navigateToSwipeScreen = {},
//           onBack = {},
//           subscriptionType = SubscriptionType.FREE,
//           navigateToTravelScreen = {},
//           navigateToVisitorsScreen = {},
//           currentPurchasedPlan = SubscriptionType.FREE
//       )


    }
}

@Composable
fun IceBreakerSection(onRefresh:()-> Unit, onItemClick:(String)-> Unit){

    val items = listOf(
        "What's your guilty pleasure?",
        "Best gift you've received?",
        "What’s your most-used emoji?",
        "What’s your biggest irrational fear?",
        "What’s your zodiac sign?",
        "Who inspires you the most and why?",
        "What’s your favorite season?",
        "What’s the last thing you Googled?"
    )

    Column (Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xffF3335D),
                            Color(0xffF33386)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        ){
            Column(Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 29.dp, vertical = 12.dp),
                    text = stringResource(Res.string.pick_a_question_to_break_ice),
                    fontFamily = PoppinsFontFamily(),
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.2.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                items.forEachIndexed { index, value ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                            .noRippleClickable { onItemClick(value) },
                        text = value,
                        fontFamily = PoppinsFontFamily(),
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                    if(index != items.lastIndex) {
                        CustomDivider()
                    }
                }


                Spacer(Modifier.height(8.5.dp))


            }
        }
        Row (Modifier
            .fillMaxWidth()
            .height(40.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Image(
                modifier = Modifier.noRippleClickable(onRefresh),
                imageVector = vectorResource(Res.drawable.ic_refresh_ice_breaker),
                contentDescription=null
            )
        }


    }


}






