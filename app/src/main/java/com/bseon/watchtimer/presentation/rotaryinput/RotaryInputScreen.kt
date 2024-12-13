package com.bseon.watchtimer.presentation.rotaryinput

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.bseon.watchtimer.TimerService
import com.bseon.watchtimer.TimerService.Companion.MAX_TIMER_DURATION
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.presentation.theme.TartOrange
import com.bseon.watchtimer.presentation.timer.TimerButton
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import com.bseon.watchtimer.utils.calculateProgress
import com.bseon.watchtimer.utils.toMinutes

@Composable
fun RotaryInputScreen(viewModel: MainViewModel) {

    val isAmbient by viewModel.ambientState.observeAsState(false)

    val timerState by viewModel.customTimerState.observeAsState(TimerState.STOPPED)
    val timeLeft by viewModel.customTimerDuration.observeAsState(TimerService.DEFAULT_TIMER_DURATION.toMinutes())

    val onPrimaryClick = remember(timerState) {
        {
            when (timerState) {
                TimerState.RUNNING -> viewModel.onTimerIntent(TimerIntent.TimerPausedIntent)
                TimerState.PAUSED -> viewModel.onTimerIntent(TimerIntent.TimerResumedIntent)
                TimerState.STOPPED -> viewModel.onTimerIntent(TimerIntent.TimerStartedIntent)
                TimerState.FINISHED -> viewModel.onTimerIntent(TimerIntent.TimerFinishedIntent)
            }
        }
    }

    val onSecondaryClick = remember {
        { viewModel.onTimerIntent(TimerIntent.TimerCancelledIntent) }
    }

    Box{
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        viewModel.onUserInteraction() // 터치 이벤트 시 초기화
                    })
                },
            progress = calculateProgress(
                currentTimerValue = timeLeft,
                maxTimerValue = MAX_TIMER_DURATION.toMinutes()
            ),
            indicatorColor = TartOrange
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                color = Color.White,
                fontSize = 50.sp,
                text = timeLeft.toString()
            )

            Spacer(modifier = Modifier.height(15.dp))

            TimerButton(
                timerState,
                isAmbient && timerState == TimerState.RUNNING,
                onPrimaryActionClick = onPrimaryClick,
                onSecondaryActionClick = onSecondaryClick,
            )

            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "App Preview")
@Composable
fun RotaryPreview() {
    RotaryInputScreen(MainViewModel(LocalContext.current))
}