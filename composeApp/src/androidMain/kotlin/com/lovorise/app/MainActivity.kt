package com.lovorise.app

import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chaintech.videoplayer.util.PlaybackPreference
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.lovorise.app.libs.audio.waveform.AmplitudeType
import com.lovorise.app.libs.audio.waveform.AudioWaveform
import com.lovorise.app.libs.audio.waveform.WaveformAlignment
import com.lovorise.app.libs.iap.InAppPurchaseAndSubscription
import com.lovorise.app.lovorise_hearts.domain.model.TransactionData
import com.lovorise.app.lovorise_hearts.presentation.components.FilterTransactionBottomSheetContent
import com.lovorise.app.lovorise_hearts.presentation.components.TransactionDetailsBottomSheetContent
import com.lovorise.app.profile.presentation.edit_profile.hideWithCompletion
import com.lovorise.app.ui.ThemeViewModel
import com.lovorise.app.util.GpsProvider
import com.lovorise.app.util.extractAndStoreReferralCodeFromUrl
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
                    App(
                        mediaPlayerWorker = null,
                        gpsProvider = gpsProvider,
                        iap = iap
                    )
//                    GiftAnimation()

//                    ScreenPreview()
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
    widthDp = 360,
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
            .padding(top = 450.dp),
//            .safeContentPadding(),
       // contentAlignment = Alignment.Center
    ){

        FilterTransactionBottomSheetContent(
            isDarkMode = false,
            onApply = {
//                sheetState.hideWithCompletion(coroutineScope){
//                    purchaseScreenModel.updateTransactionFilterSheetState(false)
//                }
            }
        )

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






