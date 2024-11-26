package com.bseon.watchtimer.presentation

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibrationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var vibrator: Vibrator

    init {
        setVibrator()
    }

    fun setVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun vibrate(duration: Long = 500) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(duration) // milliseconds
        }
    }

    fun waveVibrate(vibrationDuration: Long = 200, pauseDuration: Long = 200, repeatState: Int = 0) {
        val timings = longArrayOf(vibrationDuration, pauseDuration, vibrationDuration, pauseDuration)
        val amplitudes = intArrayOf(0, 255, 0, 255)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(timings, amplitudes, repeatState)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(timings, repeatState)
        }
    }

    fun cancelVibrate() {
        vibrator.cancel()
    }
}
