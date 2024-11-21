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

class MainViewModel: ViewModel() {

    private lateinit var timerJob: Job

    private var initialTimerDuration: Long = MIllIS_IN_FUTURE
    val customTimerDuration: MutableLiveData<Long> = MutableLiveData(initialTimerDuration)
    private var oldTime: Long = 0

    val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    fun onTimerAction(action: TimerAction, pickerState: PickerState, vibrationHelper: VibrationHelper) {
        when (action) {
            TimerAction.START -> {
                setTimerDuration(pickerIndexToDisplay(pickerState.selectedOption).toMillis())
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

    private fun setTimerDuration(duration: Long) {
        initialTimerDuration = duration
        customTimerDuration.postValue(duration)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch(start = CoroutineStart.LAZY) {
            withContext(Dispatchers.IO) {
                oldTime = System.currentTimeMillis()
                while (customTimerDuration.value!! != 0L && isActive) {
                    val delayMills = System.currentTimeMillis() - oldTime
                    if (delayMills == MINUTES_TICK_INTERVAL) {
                        customTimerDuration.postValue(customTimerDuration.value!! - delayMills)
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
        const val MIllIS_IN_FUTURE = 1800000L
        const val SECOND_TICK_INTERVAL = 1000L
        const val MINUTES_TICK_INTERVAL = 60000L
    }

}