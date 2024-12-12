package com.bseon.watchtimer.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Int.toMillis(): Long {
    return this.toLong() * 60 * 1000
}

fun Long.toMinutes(): Int {
    return (this / 1000 / 60).toInt()
}

fun calculateProgress(currentTimerValue: Int, maxTimerValue: Int): Float {
    if (maxTimerValue <= 0) {
        throw IllegalArgumentException("maxTimerValue must be greater than 0")
    }
    return (currentTimerValue.toFloat() / maxTimerValue).coerceIn(0f, 1f)
}


fun CoroutineScope.activateAfterDelay(delay: Long, action: () -> Unit): Job {
    return this.launch {
        delay(delay)
        action()
    }
}