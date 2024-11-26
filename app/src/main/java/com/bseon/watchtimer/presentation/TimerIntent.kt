package com.bseon.watchtimer.presentation

sealed class TimerIntent {
    data class TimerStartedIntent(val duration: Int) : TimerIntent()

    object TimerPausedIntent : TimerIntent()

    object TimerResumedIntent : TimerIntent()

    object TimerFinishedIntent : TimerIntent()

    object TimerCancelledIntent : TimerIntent()

}