package com.lovorise.app

//import androidx.compose.foundation.gestures.optOutOfCupertinoOverscroll
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import coil3.PlatformContext
import com.lovorise.app.database.AppDatabase
import com.lovorise.app.database.dbFileName
import io.ktor.http.ContentType
import io.ktor.http.content.ChannelWriterContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.defaultForFilePath
import io.ktor.utils.io.writeFully
import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithCString
import platform.UIKit.*
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_read
import platform.posix.*


class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}


private const val BUFFER_SIZE = 4096

@OptIn(ExperimentalForeignApi::class)
actual suspend fun bodyFromFile(filepath: String): OutgoingContent {
    val queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.convert(), 0u)




    suspend fun read(fd: Int): NSData? {
        return suspendCancellableCoroutine { continuation ->
            dispatch_read(fd, BUFFER_SIZE.toULong(), queue) { dispatchData, _ ->
                val data = dispatchData as NSData
                continuation.resume(
                    if (data.bytes != null) data else null,
                    onCancellation = null
                )
            }
        }
    }

    // Determine file size
    val fileStat = nativeHeap.alloc<stat>()
    val contentLength: Long = if (stat(filepath, fileStat.ptr) == 0) {
        fileStat.st_size
    } else {
        -1 // Error case (file doesn't exist or can't be accessed)
    }
    nativeHeap.free(fileStat)

    return ChannelWriterContent(contentLength = contentLength,contentType = ContentType.defaultForFilePath(filepath), body = {
        val fd = open(filepath, O_RDWR)
        try {
            while (true) {
                val data = read(fd) ?: break
                val bytes: CPointer<ByteVar> = data.bytes!!.reinterpret()
                writeFully(bytes, 0, data.length.toInt())
                flush()
            }
        } finally {
            close(fd)
        }
    })
}

actual fun pxToSp(px: Float, density: Density): TextUnit {
    val scale = UIScreen.mainScreen.scale.toFloat()
    val fontScale = scale * when (UIApplication.sharedApplication.preferredContentSizeCategory) {
        // Adjust these values based on your needs
        UIContentSizeCategoryExtraSmall -> 0.85f
        UIContentSizeCategorySmall -> 0.9f
        UIContentSizeCategoryMedium -> 1.0f
        UIContentSizeCategoryLarge -> 1.1f
        UIContentSizeCategoryExtraLarge -> 1.2f
        UIContentSizeCategoryExtraExtraLarge -> 1.3f
        UIContentSizeCategoryExtraExtraExtraLarge -> 1.4f
        else -> 1.0f
    }
    return (px * fontScale).sp
}

//actual fun hasSystemUINavigation(): Boolean {
//    return false // iOS doesn't have a hideable system navigation UI
//}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun isAndroid():Boolean = false

actual fun getAppVersion(context: PlatformContext): String {
    val version = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
    return version ?: "unknown"
}

private fun getDeviceIdentifier(): String {
    val name = "hw.machine"
//    var size = 0u
//    sysctlbyname(name, null, size.toULong().ptr, null, 0)
//    sysctlbyname(name, null, size.toULong().ptr, null, 0)
//    val bytes = ByteArray(size.toInt())
//    sysctlbyname(name, bytes.refTo(0), size.toULong().ptr, null, 0)
//    return NSString.stringWithCString(bytes.toCValues().ptr, encoding = 4u /* UTF-8 */) ?: "unknown"
    return ""
}

actual fun getDatabaseModule() : Module = module {
   single{ createRoomDatabase() }
}


fun createRoomDatabase(): AppDatabase {
    val dbFile = "${fileDirectory()}/$dbFileName"
    return Room.databaseBuilder<AppDatabase>(name = dbFile)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
//    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
//        directory = NSDocumentDirectory,
//        inDomain = NSUserDomainMask,
//        appropriateForURL = null,
//        create = false,
//        error = null,
//    )
//    return requireNotNull(documentDirectory).path!!
    return ""
}

//private fun getDeviceIdentifier(): String {
//    memScoped {
//        val name = "hw.machine"
//        var size = alloc<size_tVar>()
//        // First call to get the size
//        sysctlbyname(name, null, size.ptr, null, 0)
//        val byteArray = ByteArray(size.value.toInt())
//        // Second call to get the data
//        sysctlbyname(name, byteArray.refTo(0), size.ptr, null, 0)
//        return byteArray.decodeToString().trimEnd('\u0000')
//    }
//}

actual fun getDeviceName(): String {
    return  ""
//    val identifier = getDeviceIdentifier()
//    return iosDeviceMap[identifier] ?: identifier // fallback to raw identifier if not found
}


@OptIn(ExperimentalFoundationApi::class)
actual fun disableUiKitOverscroll() {

}
actual fun provideNullAndroidOverscrollConfiguration() = emptyArray<ProvidedValue<*>>()

actual fun closeApp(context: PlatformContext){
    exit(0)
}



actual fun formatToTwoDecimalPlaces(value: Float): String {
    val formatter = NSNumberFormatter().apply {
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    return formatter.stringFromNumber(NSNumber(value)) ?: value.toString()
}

actual fun updateThemeStyle(isDark:Boolean,context: PlatformContext){
    // Optionally, set the status bar style
    if (isDark) {
        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleLightContent)
    } else {
        UIApplication.sharedApplication.setStatusBarStyle(UIStatusBarStyleDarkContent)
    }
}

actual fun showPermissionScreen(context: PlatformContext) :Boolean = true
