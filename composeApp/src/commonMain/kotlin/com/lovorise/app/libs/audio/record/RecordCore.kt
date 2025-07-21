package com.lovorise.app.libs.audio.record

import coil3.PlatformContext
import com.lovorise.app.libs.audio.record.config.RecordConfig

internal expect object RecordCore {
    internal fun startRecording(config: RecordConfig,context: PlatformContext)
    internal fun stopRecording(config: RecordConfig): String
    internal fun isRecording(): Boolean
}
