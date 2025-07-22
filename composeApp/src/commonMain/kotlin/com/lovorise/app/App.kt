package com.lovorise.app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.lovorise.app.libs.iap.InAppPurchasesAndSubscriptions
import com.lovorise.app.settings.presentation.screens.SettingsScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.AddPersonScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.PrivacyAndSecurityScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.WhoCanSeeMyLocationScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.WhoCanSeeMyStoryScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.WhoCanShareMyOnlineStatusScreen
import com.lovorise.app.settings.presentation.screens.privacy_and_security.screens.WhoCanShareMyProfileScreen
import com.lovorise.app.util.CacheUtil
import com.lovorise.app.util.GpsProvider
import okio.FileSystem
import org.koin.compose.KoinContext

@Composable
fun App(
    mediaPlayerWorker: MediaPlayerWorker?,
    gpsProvider: GpsProvider,
    cacheUtil: CacheUtil? = null,
    iap: InAppPurchasesAndSubscriptions
) {

    LaunchedEffect(true){
        MediaPlayerWorkerInstance.DEFAULT = mediaPlayerWorker
        GpsProviderInstance.DEFAULT = gpsProvider
        CacheUtilInstance.DEFAULT = cacheUtil
        InAppPurchase.DEFAULT = iap
    }



//    val connectivityViewModel = remember { ConnectivityViewModel() }
//    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }


    KoinContext {
        Box {
            Navigator(SettingsScreen()) { navigator ->
//                val from = navigator.items.getOrNull(navigator.items.lastIndex - 1)
//                val to = navigator.lastItem

               // if (from is SplashScreen && (to is OnboardingScreen)) {
                    Crossfade(navigator, animationSpec = tween(durationMillis = 30)){ screen ->
                        screen.lastItem.Content()// You need to render the screen manually
                    }
//                } else {
//                    SlideTransition(
//                        navigator = navigator,
//                        animationSpec = spring(
//                            stiffness = Spring.StiffnessMedium,
//                            visibilityThreshold = IntOffset.VisibilityThreshold
//                        )
//                    )
//                }
            }
        }
    }

}

object InAppPurchase{
    var DEFAULT: InAppPurchasesAndSubscriptions? = null
}

object CacheUtilInstance{

    var DEFAULT : CacheUtil? = null
}

object MediaPlayerWorkerInstance{

    var DEFAULT : MediaPlayerWorker? = null
}

object GpsProviderInstance{
    var DEFAULT: GpsProvider? = null
}


fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1500) // 512MB
        .build()
}
