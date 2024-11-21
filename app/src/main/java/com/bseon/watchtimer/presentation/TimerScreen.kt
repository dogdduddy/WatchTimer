package com.bseon.watchtimer.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.bseon.watchtimer.R


@Composable
fun TimerScreen(viewModel: MainViewModel) {
    val pickerState = rememberPickerState(60, 30)

    val timerState by viewModel.customTimerState.observeAsState(TimerState.STOPPED)
    val timeLeft by viewModel.customTimerDuration.observeAsState(MainViewModel.MIllIS_IN_FUTURE)

    val context = LocalContext.current
    val vibrationHelper = remember { VibrationHelper(context) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (timeLeft == 0L) {
            viewModel.finishTimer()
            vibrationHelper.waveVibrate()
        }

        TimerTitle()

        Spacer(modifier = Modifier.height(15.dp))

        TimerContent(timerState, pickerState, timeLeft)

        Spacer(modifier = Modifier.height(15.dp))

        TimerButton(viewModel, timerState, pickerState, vibrationHelper)

    }
}

@Composable
fun TimerTitle() {
    Text(text = "Minute")
}

@Composable
fun TimerContent(
    timerState: TimerState,
    pickerState: PickerState,
    timeLeft: Long,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (timerState == TimerState.STOPPED) {
            Picker(
                state = pickerState,
                contentDescription = "Number Picker",
                separation = 1.dp,
            ) {
                Text(text = "${pickerIndexToDisplay(it)}", fontSize = 24.sp)
            }
        } else {
            Text (
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 24.sp,
                text = timeLeft.toMinutes().toString()
            )
        }
    }
}

@Composable
fun TimerButton(viewModel: MainViewModel, timerState: TimerState, pickerState: PickerState, vibrationHelper: VibrationHelper) {
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
                .height(30.dp)
                .width(30.dp)
                .clickable {
                    when(timerState) {
                        TimerState.RUNNING -> viewModel.pauseTimer()
                        TimerState.PAUSED -> viewModel.resumeTimer()
                        TimerState.STOPPED -> {
                            viewModel.setTimerDuration(
                                pickerIndexToDisplay(pickerState.selectedOption).toMillis()
                            )
                            viewModel.startTimer()
                        }
                        TimerState.FINISHED -> {
                            vibrationHelper.cancelVibrate()
                            viewModel.stopTimer()
                        }
                    }
                },
            contentDescription = "Run Button",

            )

        if(timerState == TimerState.PAUSED) {
            Spacer(modifier = Modifier.width(15.dp))

            Image(
                painter = painterResource(R.drawable.ic_stop_btn),
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .clickable {
                        viewModel.stopTimer()
                    },
                contentDescription = "Stop Button",
            )
        }
    }
}