package com.bseon.watchtimer.model

sealed class TimerIntent {

    data class TimerSettingIntent(val duration: Int) : TimerIntent()

    object TimerStartedIntent : TimerIntent()

    object TimerPausedIntent : TimerIntent()

    object TimerResumedIntent : TimerIntent()

    object TimerFinishedIntent : TimerIntent()

    object TimerCancelledIntent : TimerIntent()

}