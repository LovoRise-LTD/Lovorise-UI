package com.lovorise.app

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import coil3.PlatformContext
import com.lovorise.app.database.AppDatabase
import com.lovorise.app.database.dbFileName
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.defaultForFilePath
import io.ktor.util.cio.readChannel
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import java.util.Locale
import org.koin.android.ext.koin.*

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual suspend fun bodyFromFile(filepath: String): OutgoingContent {
    return object : OutgoingContent.ReadChannelContent() {

        override val contentLength: Long?
            get() = File(filepath).length()
        override val contentType: ContentType
            get() = ContentType.defaultForFilePath(filepath)
        override fun readFrom(): ByteReadChannel {
            return File(filepath).readChannel()
        }
    }
}

actual fun pxToSp(px: Float,density: Density): TextUnit{
    return with(density) { px / fontScale }.sp
}

actual fun isAndroid():Boolean = true

//actual fun hasSystemUINavigation(view: View): Boolean {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        val insets = .rootWindowInsets
//        insets?.isVisible(WindowInsets.Type.navigationBars()) == true
//    } else {
//        true // Assume navigation is visible for older Android versions
//    }
//}


actual fun getPlatform(): Platform = AndroidPlatform()

actual fun disableUiKitOverscroll() {}
@OptIn(ExperimentalFoundationApi::class)
actual fun provideNullAndroidOverscrollConfiguration(): Array<ProvidedValue<*>> = arrayOf(
    LocalOverscrollConfiguration provides null
)

actual fun closeApp(context:PlatformContext){
    (context as Activity).finish()
}

actual fun formatToTwoDecimalPlaces(value: Float): String {
    return String.format(locale = Locale.getDefault(),"%.2f", value)
}

actual fun getDatabaseModule() : Module = module {
    single {
        createRoomDatabase(androidContext().applicationContext)
    }
}


fun createRoomDatabase(ctx: Context): AppDatabase {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room.databaseBuilder<AppDatabase>(ctx, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

actual fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    val model = Build.MODEL
    return if (model.startsWith(manufacturer, ignoreCase = true)) {
        model
    } else {
        "$manufacturer $model"
    }
}



actual fun getAppVersion(context: PlatformContext): String {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName ?: "unknown"
}

actual fun updateThemeStyle(isDark:Boolean,context: PlatformContext){
    val activity = (context as Activity)
    val window = activity.window
    WindowCompat.getInsetsController(window, activity.window.decorView).isAppearanceLightStatusBars = !isDark
    println("done updating theme style")
}

actual fun showPermissionScreen(context: PlatformContext):Boolean{
    return !hasNotificationPermission(context)
}

fun hasNotificationPermission(context: PlatformContext): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}


