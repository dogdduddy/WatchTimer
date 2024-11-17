package com.bseon.watchtimer.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {

    private lateinit var timerJob: Job

    val customTimerDuration: MutableLiveData<Long> = MutableLiveData(MIllIS_IN_FUTURE)
    private var oldTime: Long = 0

    val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    fun setTimerDuration(duration: Long) {
        customTimerDuration.postValue(duration)
    }

    fun startTimer() {
        timerJob = viewModelScope.launch(start = CoroutineStart.LAZY) {
            withContext(Dispatchers.IO) {
                oldTime = System.currentTimeMillis()
                while (customTimerDuration.value!! > TICK_INTERVAL && isActive) {
                    val delayMills = System.currentTimeMillis() - oldTime
                    if (delayMills == TICK_INTERVAL) {
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

    fun pauseTimer() {
        if(timerJob.isActive) {
            customTimerState.postValue(TimerState.PAUSED)
            timerJob.cancel()
        }
    }
    fun resumeTimer() {
        if (customTimerState.value == TimerState.PAUSED) {
            startTimer()
        }
    }
    fun stopTimer() {
        if (timerJob.isActive) {
            timerJob.cancel()
        }
        customTimerState.postValue(TimerState.STOPPED)
        customTimerDuration.postValue(MIllIS_IN_FUTURE)
    }

    companion object {
        const val MIllIS_IN_FUTURE = 1800000L
        const val TICK_INTERVAL = 1000L
    }

    enum class TimerState {
        RUNNING,
        PAUSED,
        STOPPED
    }
}