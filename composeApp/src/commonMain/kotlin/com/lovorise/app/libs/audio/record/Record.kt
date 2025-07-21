package com.lovorise.app.libs.audio.record

import coil3.PlatformContext
import com.lovorise.app.libs.audio.record.config.RecordConfig

object Record {
    private var recordConfig = RecordConfig()

    fun startRecording(context: PlatformContext) {
        RecordCore.startRecording(recordConfig,context)
    }

    fun stopRecording(): String {
        return RecordCore.stopRecording(recordConfig)
    }

    fun isRecording(): Boolean {
        return RecordCore.isRecording()
    }

    fun setConfig(config: RecordConfig) {
        recordConfig = config
    }
}
