@file:Suppress("MatchingDeclarationName")

package com.lovorise.app.libs.audio.record

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import coil3.PlatformContext
import com.lovorise.app.libs.audio.record.config.OutputFormat
import com.lovorise.app.libs.audio.record.config.RecordConfig
import com.lovorise.app.libs.audio.record.error.NoOutputFileException
import com.lovorise.app.libs.audio.record.error.PermissionMissingException
import com.lovorise.app.libs.audio.record.error.RecordFailException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal actual object RecordCore {
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private var recordingThread: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var recorder: MediaRecorder? = null
    private var output: String? = null
    @Volatile
    private var myRecordingState: RecordingState = RecordingState.IDLE


    @Throws(RecordFailException::class)
    internal actual fun startRecording(config: RecordConfig,context: PlatformContext) {
        checkPermission(context)
        output = config.getOutput(context)
        File(output!!).parentFile?.mkdirs() //Ensure the output file path exists before recording starts
        when(config.outputFormat) {
            OutputFormat.MPEG_4 -> {
                recorder = createMediaRecorder(config,context)

                recorder?.apply {
                    runCatching {
                        prepare()
                    }.onFailure {
                        throw RecordFailException()
                    }

                    setOnErrorListener { _, _, _ ->
                        stopRecording(config)
                    }

                    start()
                    myRecordingState = RecordingState.RECORDING
                }
            }
            OutputFormat.WAV -> {
                val bufferSize = AudioRecord.getMinBufferSize(
                    config.sampleRate,
                    config.outputFormat.toMediaRecorderOutputFormat(),
                    config.audioEncoder.toMediaRecorderAudioEncoder()
                )

                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    config.sampleRate,
                    config.outputFormat.toMediaRecorderOutputFormat(),
                    config.audioEncoder.toMediaRecorderAudioEncoder(),
                    bufferSize
                )

                audioRecord?.apply {
                    startRecording()
                    myRecordingState = RecordingState.RECORDING

                    recordingJob = recordingThread.launch {
                        try {
                            writeAudioDataToFile(bufferSize, config.sampleRate)
                        } catch (e: Exception) {
                            Log.e("RecordCore", "Recording failed", e)
                        }
                    }
                }
            }
        }
    }

    @Throws(NoOutputFileException::class)
    internal actual fun stopRecording(config: RecordConfig): String {
        myRecordingState = RecordingState.IDLE
        when (config.outputFormat) {
            OutputFormat.MPEG_4 -> {
                recorder?.apply {
                    try {
                        stop()
                    } catch (e: Exception) {
                        Log.e("RecordCore", "Error stopping MediaRecorder", e)
                    } finally {
                        release()
                    }
                }

                return output.also {
                    output = null
                    recorder = null
                } ?: throw NoOutputFileException()
            }
            OutputFormat.WAV -> {
                recordingJob?.cancel()
                audioRecord?.apply {
                    try {
                        stop()
                    } catch (e: Exception) {
                        Log.e("RecordCore", "Error stopping AudioRecord", e)
                    } finally {
                        release()
                    }
                }
                return output ?: throw NoOutputFileException()
            }
        }
    }

    internal actual fun isRecording(): Boolean = myRecordingState == RecordingState.RECORDING

    private fun createMediaRecorder(config: RecordConfig,context: PlatformContext) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(config.outputFormat.toMediaRecorderOutputFormat())
            setOutputFile(output)
            setAudioEncoder(config.audioEncoder.toMediaRecorderAudioEncoder())
        }

    private fun checkPermission(context: PlatformContext) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                RECORD_AUDIO
            ) != PERMISSION_GRANTED
        ) {
            throw PermissionMissingException()
        }
    }

    private fun writeAudioDataToFile(bufferSize: Int, sampleRate: Int) {
        val data = ByteArray(bufferSize)
        var totalAudioLength = 0

        FileOutputStream(output).use { fos ->
            // Write a placeholder for the WAV file header
            fos.write(ByteArray(44))

            // Write PCM data
            while (isRecording()) {
                val read = audioRecord?.read(data, 0, data.size) ?: 0
                if (read > 0) {
                    fos.write(data, 0, read)
                    totalAudioLength += read
                }
            }

            // Update WAV header after recording is done
            fos.channel.position(0) // Rewind to start of file
            fos.writeWavHeader(sampleRate, totalAudioLength + 36) // Data size + 36 bytes for header
        }
    }
}