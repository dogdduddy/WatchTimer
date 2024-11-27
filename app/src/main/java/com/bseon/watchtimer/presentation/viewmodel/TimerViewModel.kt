package com.bseon.watchtimer.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState

interface TimerViewModel {
    val customTimerState: LiveData<TimerState>
    val customTimerDuration: LiveData<Int>
    fun onTimerIntent(intent: TimerIntent)
}