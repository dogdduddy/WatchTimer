package com.bseon.watchtimer.presentation.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bseon.watchtimer.R
import com.bseon.watchtimer.presentation.TimerState

@Composable
fun TimerButton(timerState: TimerState, onPrimaryActionClick: () -> Unit, onSecondaryActionClick: () -> Unit) {
    Row {
        val painterResource = when(timerState) {
            TimerState.RUNNING -> R.drawable.ic_pause_btn
            TimerState.PAUSED -> R.drawable.ic_run_btn
            TimerState.STOPPED -> R.drawable.ic_run_btn
            TimerState.FINISHED -> R.drawable.ic_stop_btn
        }

        Image(
            painter = painterResource(painterResource),
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable { onPrimaryActionClick() },
            contentDescription = "Run Button",
        )

        if(timerState == TimerState.PAUSED) {
            Spacer(modifier = Modifier.width(15.dp))

            Image(
                painter = painterResource(R.drawable.ic_stop_btn),
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { onSecondaryActionClick() },
                contentDescription = "Stop Button",
            )
        }
    }
}