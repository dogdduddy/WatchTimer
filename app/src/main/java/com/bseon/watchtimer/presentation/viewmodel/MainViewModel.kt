package com.bseon.watchtimer.presentation.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bseon.watchtimer.TimerService
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.utils.VibrationHelper
import com.bseon.watchtimer.utils.activateAfterDelay
import com.bseon.watchtimer.utils.toMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val vibrationHelper = VibrationHelper(context)

    private var initialTimerDuration: Int = TimerService.MIllIS_IN_FUTURE.toMinutes()
    val customTimerDuration: MutableLiveData<Int> = MutableLiveData(initialTimerDuration)

    val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    private var ambientJob: Job? = null
    val ambientState = MutableLiveData<Boolean>(false)

    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val timeElapsed = intent?.getIntExtra(TimerService.TIME_DURATION, 0) ?: 0
            customTimerDuration.postValue(timeElapsed)
        }
    }
    private val finishedReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                finishedReceiver()
            }
        }
    init {
        val finishedFilter = IntentFilter()
        finishedFilter.addAction(TimerService.TIMER_FINISHED)

        val filter = IntentFilter(TimerService.TIMER_TICK)

        context.registerReceiver(timerReceiver, filter, Context.RECEIVER_EXPORTED)
        context.registerReceiver(finishedReceiver, finishedFilter, Context.RECEIVER_EXPORTED)

        activeAmbientModeAfterDelay()
    }

    fun onTimerIntent(intent: TimerIntent) {
        onUserInteraction()
        when (intent) {
            is TimerIntent.TimerSettingIntent -> setTimerDuration(intent.duration)
            TimerIntent.TimerStartedIntent -> startTimer()
            TimerIntent.TimerPausedIntent -> pauseTimer()
            TimerIntent.TimerResumedIntent -> resumeTimer()
            TimerIntent.TimerCancelledIntent -> cancelTimer()
            TimerIntent.TimerFinishedIntent -> finishTimer()
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

    private fun finishedReceiver() {
        vibrationHelper.waveVibrate()
        customTimerState.postValue(TimerState.FINISHED)
        customTimerDuration.postValue(0)
    }

    private fun finishTimer() {
        vibrationHelper.cancelVibrate()
        customTimerState.postValue(TimerState.STOPPED)
        customTimerDuration.postValue(initialTimerDuration)
    }


    private fun startForegroundService(actionString: String) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = actionString
            putExtra(TimerService.TIME_DURATION, customTimerDuration.value!!)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun onUserInteraction() {
        setAmbientState(false)
        activeAmbientModeAfterDelay()
    }

    private fun setAmbientState(isAmbient: Boolean) {
        ambientState.postValue(isAmbient)
    }
    private fun activeAmbientModeAfterDelay(delay: Long = 1000L * 15) {
        ambientJob?.cancel()

        ambientJob = viewModelScope.activateAfterDelay(delay) {
            setAmbientState(true)
        }
    }


    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(timerReceiver)
        context.unregisterReceiver(finishedReceiver)
        ambientJob?.cancel()
    }
}