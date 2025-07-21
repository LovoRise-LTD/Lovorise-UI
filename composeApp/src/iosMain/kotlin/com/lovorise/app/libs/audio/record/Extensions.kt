package com.lovorise.app.libs.audio.record

import com.lovorise.app.libs.audio.record.config.OutputFormat
import com.lovorise.app.libs.audio.record.config.OutputLocation
import com.lovorise.app.libs.audio.record.config.RecordConfig
import com.lovorise.app.libs.audio.record.error.NoOutputFileException
import platform.CoreAudioTypes.AudioFormatID
import platform.CoreAudioTypes.kAudioFormatLinearPCM
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.temporaryDirectory
import kotlin.time.TimeSource

internal fun RecordConfig.getOutput(): String {
    val timestamp = TimeSource.Monotonic.markNow().toString()
    val fileName = "${timestamp}${outputFormat.extension}"

    return when (this.outputLocation) {
        OutputLocation.Cache -> "${NSFileManager.defaultManager.temporaryDirectory.path}/$fileName"
        OutputLocation.Internal -> {
            val urls = NSFileManager.defaultManager.URLsForDirectory(
                NSDocumentDirectory,
                NSUserDomainMask
            )
            val documentsURL = urls.first() as? NSURL ?: throw NoOutputFileException()
            "${documentsURL.path!!}/$fileName"
        }

        is OutputLocation.Custom -> "${this.outputLocation.path}/$fileName"
    }
}

internal fun OutputFormat.toAVFormatID(): AudioFormatID = when (this) {
    OutputFormat.MPEG_4 -> kAudioFormatMPEG4AAC
    OutputFormat.WAV -> kAudioFormatLinearPCM
}