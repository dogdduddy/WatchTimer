package com.bseon.watchtimer.presentation.pickerscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.bseon.watchtimer.presentation.WearApp
import com.bseon.watchtimer.presentation.timer.TimerButton
import com.bseon.watchtimer.presentation.timer.TimerTitle
import com.bseon.watchtimer.presentation.timer.navigationButton
import com.bseon.watchtimer.model.TimerIntent
import com.bseon.watchtimer.model.TimerState
import com.bseon.watchtimer.presentation.viewmodel.FakeMainViewModel
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import com.bseon.watchtimer.presentation.viewmodel.TimerViewModel


@Composable
fun PickerScreen(navController: NavController) {
    val pickerState = rememberPickerState(60, 30)

    val viewModel: TimerViewModel = if (LocalInspectionMode.current) {
        FakeMainViewModel()
    } else {
        hiltViewModel<MainViewModel>()
    }

    val timerState by viewModel.customTimerState.observeAsState(TimerState.STOPPED)
    val timeLeft by viewModel.customTimerDuration.observeAsState(MainViewModel.MIllIS_IN_FUTURE)

    LaunchedEffect(pickerState.selectedOption) {
        viewModel.onTimerIntent(TimerIntent.TimerSettingIntent(pickerState.selectedOption))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (timeLeft == 0) { viewModel.onTimerIntent(TimerIntent.TimerFinishedIntent) }
        TimerTitle()

        Spacer(modifier = Modifier.height(5.dp))

        TimerContent(timerState, pickerState, timeLeft)

        Spacer(modifier = Modifier.height(5.dp))

        TimerButton(timerState,
            onPrimaryActionClick = {
                when(timerState) {
                    TimerState.RUNNING -> viewModel.onTimerIntent(TimerIntent.TimerPausedIntent)
                    TimerState.PAUSED -> viewModel.onTimerIntent(TimerIntent.TimerResumedIntent)
                    TimerState.STOPPED -> viewModel.onTimerIntent(TimerIntent.TimerStartedIntent)
                    TimerState.FINISHED -> viewModel.onTimerIntent(TimerIntent.TimerCancelledIntent)
                }
            },
            onSecondaryActionClick = {
                viewModel.onTimerIntent(TimerIntent.TimerCancelledIntent)
            }
        )
    }
}

@Composable
fun TimerContent(
    timerState: TimerState,
    pickerState: PickerState,
    timeLeft: Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (timerState == TimerState.STOPPED) {
            Picker(
                state = pickerState,
                contentDescription = "Number Picker",
            ) {
                Text(text = "${it.inc()}", fontSize = 50.sp)
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
    WearApp()
}