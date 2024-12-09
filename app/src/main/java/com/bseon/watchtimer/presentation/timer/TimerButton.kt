package com.bseon.watchtimer.presentation.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bseon.watchtimer.R
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.utils.StateLessImage

@Composable
fun TimerButton(
    timerState: TimerState,
    isInVisible: Boolean,
    onPrimaryActionClick: () -> Unit,
    onSecondaryActionClick: () -> Unit,
) {

   val primaryModifier = remember {
        Modifier
            .size(30.dp)
            .clip(CircleShape)
            .clickable { onPrimaryActionClick() }
    }

    val secondaryModifier = remember {
        Modifier
            .size(30.dp)
            .clip(CircleShape)
            .clickable { onSecondaryActionClick() }
    }

    Row(
        modifier = Modifier.alpha(if (isInVisible) 0f else 1f),
    ) {
        val painterResource = when (timerState) {
                TimerState.RUNNING -> R.drawable.ic_pause_btn
                TimerState.PAUSED -> R.drawable.ic_run_btn
                TimerState.STOPPED -> R.drawable.ic_run_btn
                TimerState.FINISHED -> R.drawable.ic_stop_btn
            }

        StateLessImage(
            resourceId = painterResource,
            modifier = primaryModifier,
            contentDescription = "Run Button",
        )

        if(timerState == TimerState.PAUSED) {
            Spacer(modifier = Modifier.width(15.dp))

            StateLessImage(
                resourceId = R.drawable.ic_stop_btn,
                modifier = secondaryModifier,
                contentDescription = "Stop Button",
            )
        }
    }
}