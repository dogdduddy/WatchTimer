package com.bseon.watchtimer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FakeMainViewModel : TimerViewModel {
    override val customTimerState: LiveData<TimerState> = MutableLiveData(TimerState.STOPPED)

    override val customTimerDuration: LiveData<Int> = MutableLiveData(30)

    override fun onTimerIntent(intent: TimerIntent) {
        // Fake 로직
    }
}