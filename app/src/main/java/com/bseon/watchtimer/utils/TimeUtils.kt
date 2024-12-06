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

fun CoroutineScope.activateAfterDelay(delay: Long, action: () -> Unit): Job {
    return this.launch {
        delay(delay)
        action()
    }
}