package com.bseon.watchtimer.presentation

import androidx.lifecycle.LiveData

interface TimerViewModel {
    val customTimerState: LiveData<TimerState>
    val customTimerDuration: LiveData<Int>
    fun onTimerIntent(intent: TimerIntent)
}