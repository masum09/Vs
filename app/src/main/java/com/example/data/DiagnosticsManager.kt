package com.example.data

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

enum class TorchMode {
    OFF,
    SOLID,
    STROBE,
    SOS
}

enum class AudioChannel {
    STEREO,
    LEFT_ONLY,
    RIGHT_ONLY
}

data class DiagnosticsState(
    val isTorchOn: Boolean = false,
    val torchMode: TorchMode = TorchMode.OFF,
    val torchError: String? = null,
    val isVibrating: Boolean = false,
    val isAudioPlaying: Boolean = false,
    val audioFrequencyHz: Int = 440,
    val audioVolumePercent: Int = 80,
    val audioChannel: AudioChannel = AudioChannel.STEREO
)

class DiagnosticsManager(private val context: Context) {

    private val _state = MutableStateFlow(DiagnosticsState())
    val state: StateFlow<DiagnosticsState> = _state.asStateFlow()

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
    private var cameraIdWithFlash: String? = null

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vm?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private var audioJob: Job? = null
    private var strobeJob: Job? = null
    private var audioTrack: AudioTrack? = null

    init {
        try {
            cameraManager?.let { cm ->
                for (id in cm.cameraIdList) {
                    val chars = cm.getCameraCharacteristics(id)
                    val flashAvailable = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
                    val facing = chars.get(CameraCharacteristics.LENS_FACING)
                    if (flashAvailable && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraIdWithFlash = id
                        break
                    }
                }
                if (cameraIdWithFlash == null && cm.cameraIdList.isNotEmpty()) {
                    cameraIdWithFlash = cm.cameraIdList[0]
                }
            }
        } catch (_: Exception) {
            // Flash not accessible in this context
        }
    }

    // --- TORCH / FLASHLIGHT CONTROLS ---

    fun setTorchMode(mode: TorchMode, strobeSpeedHz: Int = 5, scope: CoroutineScope) {
        strobeJob?.cancel()
        strobeJob = null

        val camId = cameraIdWithFlash
        if (camId == null) {
            _state.value = _state.value.copy(
                isTorchOn = mode != TorchMode.OFF,
                torchMode = mode,
                torchError = "Hardware camera flash not detected on this unit (Simulating torch state)"
            )
            return
        }

        try {
            when (mode) {
                TorchMode.OFF -> {
                    cameraManager?.setTorchMode(camId, false)
                    _state.value = _state.value.copy(isTorchOn = false, torchMode = TorchMode.OFF, torchError = null)
                }
                TorchMode.SOLID -> {
                    cameraManager?.setTorchMode(camId, true)
                    _state.value = _state.value.copy(isTorchOn = true, torchMode = TorchMode.SOLID, torchError = null)
                }
                TorchMode.STROBE -> {
                    _state.value = _state.value.copy(isTorchOn = true, torchMode = TorchMode.STROBE, torchError = null)
                    strobeJob = scope.launch(Dispatchers.Default) {
                        var flashOn = false
                        val intervalMs = (1000L / strobeSpeedHz.coerceIn(1, 20)).coerceAtLeast(30L)
                        while (isActive) {
                            flashOn = !flashOn
                            try {
                                cameraManager?.setTorchMode(camId, flashOn)
                            } catch (_: Exception) {}
                            delay(intervalMs)
                        }
                        try {
                            cameraManager?.setTorchMode(camId, false)
                        } catch (_: Exception) {}
                    }
                }
                TorchMode.SOS -> {
                    _state.value = _state.value.copy(isTorchOn = true, torchMode = TorchMode.SOS, torchError = null)
                    strobeJob = scope.launch(Dispatchers.Default) {
                        // S: ... O: --- S: ...
                        val dot = 150L
                        val dash = 450L
                        val gap = 150L
                        val letterGap = 400L
                        val wordGap = 1200L

                        val pattern = listOf(
                            dot, dot, dot, // S
                            dash, dash, dash, // O
                            dot, dot, dot // S
                        )

                        while (isActive) {
                            for (i in pattern.indices) {
                                val dur = pattern[i]
                                try { cameraManager?.setTorchMode(camId, true) } catch (_: Exception) {}
                                delay(dur)
                                try { cameraManager?.setTorchMode(camId, false) } catch (_: Exception) {}
                                delay(if (i == 2 || i == 5) letterGap else gap)
                            }
                            delay(wordGap)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isTorchOn = false,
                torchMode = TorchMode.OFF,
                torchError = "Flash access error: ${e.message}"
            )
        }
    }

    // --- VIBRATION PATTERNS ---

    fun testVibration(patternType: String, scope: CoroutineScope) {
        if (vibrator == null || !vibrator.hasVibrator()) {
            return
        }

        scope.launch(Dispatchers.Default) {
            _state.value = _state.value.copy(isVibrating = true)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    when (patternType) {
                        "IMPACT" -> {
                            // Heavy Dragonslayer slash impact
                            vibrator.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 180, 50, 220),
                                    intArrayOf(0, 255, 0, 200),
                                    -1
                                )
                            )
                        }
                        "HEARTBEAT" -> {
                            // Brand of sacrifice pulsating double beat
                            vibrator.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 90, 80, 140, 400, 90, 80, 140),
                                    intArrayOf(0, 180, 0, 255, 0, 180, 0, 255),
                                    -1
                                )
                            )
                        }
                        "ROAR" -> {
                            // Berserk rising surge
                            vibrator.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 60, 60, 80, 80, 120, 150),
                                    intArrayOf(0, 50, 90, 140, 190, 230, 255),
                                    -1
                                )
                            )
                        }
                        else -> {
                            // Sharp tick
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
                            )
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(200)
                }
            } catch (_: Exception) {}
            delay(800)
            _state.value = _state.value.copy(isVibrating = false)
        }
    }

    fun stopVibration() {
        try {
            vibrator?.cancel()
            _state.value = _state.value.copy(isVibrating = false)
        } catch (_: Exception) {}
    }

    // --- AUDIO TONE SYNTHESIS ---

    fun setAudioFrequency(freq: Int) {
        _state.value = _state.value.copy(audioFrequencyHz = freq.coerceIn(50, 12000))
    }

    fun setAudioVolume(volPercent: Int) {
        _state.value = _state.value.copy(audioVolumePercent = volPercent.coerceIn(0, 100))
    }

    fun setAudioChannel(channel: AudioChannel) {
        _state.value = _state.value.copy(audioChannel = channel)
    }

    fun toggleAudioTone(scope: CoroutineScope) {
        if (_state.value.isAudioPlaying) {
            stopAudioTone()
        } else {
            startAudioTone(scope)
        }
    }

    fun startAudioTone(scope: CoroutineScope) {
        stopAudioTone()
        _state.value = _state.value.copy(isAudioPlaying = true)

        audioJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 44100
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack = track
            track.play()

            val samplesPerChunk = bufferSize / 4 // 2 channels * 2 bytes = 4 bytes per stereo frame
            val buffer = ShortArray(samplesPerChunk * 2)
            var phase = 0.0

            try {
                while (isActive && _state.value.isAudioPlaying) {
                    val currentFreq = _state.value.audioFrequencyHz.toDouble()
                    val volume = (_state.value.audioVolumePercent / 100.0) * 0.75
                    val channel = _state.value.audioChannel
                    val phaseIncrement = (2.0 * Math.PI * currentFreq) / sampleRate

                    for (i in 0 until samplesPerChunk) {
                        val sampleVal = (sin(phase) * Short.MAX_VALUE * volume).toInt().toShort()
                        val leftSample = if (channel == AudioChannel.STEREO || channel == AudioChannel.LEFT_ONLY) sampleVal else 0
                        val rightSample = if (channel == AudioChannel.STEREO || channel == AudioChannel.RIGHT_ONLY) sampleVal else 0

                        buffer[i * 2] = leftSample
                        buffer[i * 2 + 1] = rightSample

                        phase += phaseIncrement
                        if (phase > 2.0 * Math.PI) {
                            phase -= 2.0 * Math.PI
                        }
                    }

                    track.write(buffer, 0, buffer.size)
                }
            } catch (_: Exception) {
            } finally {
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        }
    }

    fun stopAudioTone() {
        audioJob?.cancel()
        audioJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        _state.value = _state.value.copy(isAudioPlaying = false)
    }

    fun releaseAll() {
        strobeJob?.cancel()
        strobeJob = null
        audioJob?.cancel()
        audioJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        val camId = cameraIdWithFlash
        if (camId != null) {
            try {
                cameraManager?.setTorchMode(camId, false)
            } catch (_: Exception) {}
        }
        stopVibration()
    }
}
