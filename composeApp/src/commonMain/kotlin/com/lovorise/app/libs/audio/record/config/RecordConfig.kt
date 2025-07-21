package com.lovorise.app.libs.audio.record.config

data class RecordConfig(
    val outputLocation: OutputLocation = OutputLocation.Cache,
    val outputFormat: OutputFormat = OutputFormat.MPEG_4,
    val audioEncoder: AudioEncoder = AudioEncoder.AAC,
    val sampleRate: Int = 44100
)

sealed class OutputFormat(val extension: String) {
    data object MPEG_4 : OutputFormat(".mp4")
    data object WAV: OutputFormat(".wav")
}

sealed class AudioEncoder {
    data object AAC : AudioEncoder()
    data object PCM_16BIT: AudioEncoder()
}

sealed class OutputLocation {
    data object Cache : OutputLocation()
    data object Internal : OutputLocation()
    data class Custom(val path: String) : OutputLocation()
}
