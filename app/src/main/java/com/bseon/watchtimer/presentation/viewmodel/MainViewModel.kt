package com.bseon.watchtimer.presentation.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bseon.watchtimer.TimerService
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.utils.VibrationHelper
import com.bseon.watchtimer.utils.toMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    private val vibrationHelper: VibrationHelper
): ViewModel(), TimerViewModel {

    private lateinit var timerJob: Job

    private var initialTimerDuration: Int = TimerService.MIllIS_IN_FUTURE.toMinutes()
    override val customTimerDuration: MutableLiveData<Int> = MutableLiveData(initialTimerDuration)

    override val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val timeElapsed = intent?.getIntExtra(TimerService.TIME_DURATION, 0) ?: 0
            customTimerDuration.postValue(timeElapsed)
        }
    }
    private val finishedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                finishTimer()
            }
        }
    init {
        val finishedFilter = IntentFilter()
        finishedFilter.addAction(TimerService.TIMER_FINISHED)

        val filter = IntentFilter(TimerService.TIMER_TICK)

        context.registerReceiver(timerReceiver, filter)
        context.registerReceiver(finishedReceiver, finishedFilter)
    }

    override fun onTimerIntent(intent: TimerIntent) {
        when (intent) {
            is TimerIntent.TimerSettingIntent -> setTimerDuration(intent.duration.inc())
            TimerIntent.TimerStartedIntent -> startTimer()
            TimerIntent.TimerPausedIntent -> pauseTimer()
            TimerIntent.TimerResumedIntent -> resumeTimer()
            TimerIntent.TimerCancelledIntent -> cancelTimer()
            TimerIntent.TimerFinishedIntent -> null
        }
    }

    private fun setTimerDuration(duration: Int) {
        initialTimerDuration = duration
        customTimerDuration.postValue(duration)
    }

    private fun startTimer() {
        startForegroundService(TimerService.TIMER_STARTED)
        customTimerState.postValue(TimerState.RUNNING)
    }

    private fun pauseTimer() {
        startForegroundService(TimerService.TIMER_PAUSED)
        customTimerState.postValue(TimerState.PAUSED)
    }

    private fun resumeTimer() {
        if (customTimerState.value == TimerState.PAUSED) {
            startForegroundService(TimerService.TIMER_RESUMED)
            customTimerState.postValue(TimerState.RUNNING)
        }
    }

    private fun cancelTimer() {
        startForegroundService(TimerService.TIMER_CANCELLED)
        customTimerState.postValue(TimerState.STOPPED)
        customTimerDuration.postValue(initialTimerDuration)
    }

    private fun finishTimer() {
        vibrationHelper.cancelVibrate()
        customTimerState.postValue(TimerState.FINISHED)
        customTimerDuration.postValue(0)
    }


    private fun startForegroundService(actionString: String) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = actionString
            putExtra(TimerService.TIME_DURATION, customTimerDuration.value!!)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(timerReceiver)
    }
}