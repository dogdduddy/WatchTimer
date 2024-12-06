package com.bseon.watchtimer.presentation.timer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.wear.compose.material.Text

@Composable
fun TimerTitle(isInVisible: Boolean) {
    Text(
        modifier = Modifier.alpha(if (isInVisible) 0f else 1f),
        text = "Minute"
    )
}