package com.bseon.watchtimer.presentation.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.wear.compose.material.Text

@Composable
fun TimerTitle(
    modifier : Modifier = Modifier,
    isInVisible: Boolean,
) {
    val combinedModifier = remember(modifier) {
        modifier.alpha(if (isInVisible) 0f else 1f)
    }

    Text(
        modifier = combinedModifier,
        text = "Minute"
    )
}