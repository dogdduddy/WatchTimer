package com.bseon.watchtimer.utils

fun Int.toMillis(): Long {
    return this.toLong() * 60 * 1000
}

fun Long.toMinutes(): Int {
    return (this / 1000 / 60).toInt()
}