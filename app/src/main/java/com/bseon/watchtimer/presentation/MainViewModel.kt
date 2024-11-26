package com.bseon.watchtimer.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.wear.compose.material.PickerState
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainViewModel: ViewModel() {

    private lateinit var timerJob: Job

    private var initialTimerDuration: Int = MIllIS_IN_FUTURE
    open val customTimerDuration: MutableLiveData<Int> = MutableLiveData(initialTimerDuration)
    private var oldTime: Long = 0

    open val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    fun onTimerAction(action: TimerAction, vibrationHelper: VibrationHelper) {
        when (action) {
            TimerAction.START -> {
                setTimerDuration(pickerIndexToDisplay(customTimerDuration.value!!))
                startTimer()
            }
            TimerAction.PAUSED -> pauseTimer()
            TimerAction.RESUME -> resumeTimer()
            TimerAction.STOP -> {
                vibrationHelper.cancelVibrate()
                stopTimer()
            }
            TimerAction.FINISH -> {
                vibrationHelper.vibrate()
                finishTimer()
            }
        }
    }

    private fun setTimerDuration(duration: Int) {
        initialTimerDuration = duration
        customTimerDuration.postValue(duration)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch(start = CoroutineStart.LAZY) {
            withContext(Dispatchers.IO) {
                oldTime = System.currentTimeMillis()
                while (customTimerDuration.value!! != 0 && isActive) {
                    val delayMills = System.currentTimeMillis() - oldTime
                    if (delayMills == MINUTES_TICK_INTERVAL) {
                        customTimerDuration.postValue(customTimerDuration.value!! - delayMills.toMinutes())
                        oldTime = System.currentTimeMillis()
                    }
                }
            }
        }

        if(timerJob.isActive.not()) {
            customTimerState.postValue(TimerState.RUNNING)
            timerJob.start()
        }
    }

    private fun pauseTimer() {
        if(timerJob.isActive) {
            customTimerState.postValue(TimerState.PAUSED)
            timerJob.cancel()
        }
    }
    private fun resumeTimer() {
        if (customTimerState.value == TimerState.PAUSED) {
            startTimer()
        }
    }
    private fun stopTimer() {
        if (timerJob.isActive) {
            timerJob.cancel()
        }
        customTimerState.postValue(TimerState.STOPPED)
        customTimerDuration.postValue(initialTimerDuration)
    }
    private fun finishTimer() {
        customTimerState.postValue(TimerState.FINISHED)
        customTimerDuration.postValue(0)
    }

    companion object {
        const val MIllIS_IN_FUTURE = 30
        const val SECOND_TICK_INTERVAL = 1000L
        const val MINUTES_TICK_INTERVAL = 60000L
    }

}