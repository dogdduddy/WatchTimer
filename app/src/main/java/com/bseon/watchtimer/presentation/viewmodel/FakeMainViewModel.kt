package com.bseon.watchtimer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState

class FakeMainViewModel : TimerViewModel {
    override val customTimerState: LiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    override val customTimerDuration: LiveData<Int> = MutableLiveData(30)

    override fun onTimerIntent(intent: TimerIntent) {
        // Fake 로직
    }
}