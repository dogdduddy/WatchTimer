package com.bseon.watchtimer.presentation.pickerscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.bseon.watchtimer.TimerService
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.presentation.AnimatedDimScreen
import com.bseon.watchtimer.presentation.timer.NavigationButton
import com.bseon.watchtimer.presentation.timer.TimerButton
import com.bseon.watchtimer.presentation.timer.TimerTitle
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import com.bseon.watchtimer.utils.toMinutes


@Composable
fun PickerScreen(viewModel: MainViewModel, onNavigateToNextPage: () -> Unit) {
    val pickerState = rememberPickerState(60, 30)

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

    LaunchedEffect(pickerState.selectedOption) {
        viewModel.onTimerIntent(TimerIntent.TimerSettingIntent(pickerState.selectedOption))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    viewModel.onUserInteraction() // 터치 이벤트 시 초기화
                })
            }
    ) {

        TimerTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 25.dp),
            isInVisible = isAmbient && timerState == TimerState.RUNNING
        )

        TimerContent(
            modifier = Modifier.align(Alignment.Center),
            timerState = timerState,
            pickerState = pickerState,
            timeLeft = timeLeft
        )

        TimerButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp),
            timerState = timerState,
            isAmbient && timerState == TimerState.RUNNING,
            onPrimaryActionClick = onPrimaryClick,
            onSecondaryActionClick = onSecondaryClick,
        )

        NavigationButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        ) { onNavigateToNextPage() }

        AnimatedDimScreen(
            shouldDim = isAmbient && timerState != TimerState.RUNNING
        )
    }
}

@Composable
fun TimerContent(
    modifier: Modifier = Modifier,
    timerState: TimerState,
    pickerState: PickerState,
    timeLeft: Int,
) {
    val combinedModifier = remember(modifier) {
        modifier.fillMaxWidth()
            .height(90.dp)
    }

    Box(
        modifier = combinedModifier,
        contentAlignment = Alignment.Center,
    ) {
        if (timerState == TimerState.STOPPED) {
            Picker(
                state = pickerState,
                contentDescription = "Number Picker",
            ) {
                Text(text = "$it", fontSize = 50.sp)
            }
        } else {
            Text (
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 50.sp,
                text = timeLeft.toString()
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "App Preview")
@Composable
fun PickerPreview() {
    PickerScreen(MainViewModel(LocalContext.current)) {}
}