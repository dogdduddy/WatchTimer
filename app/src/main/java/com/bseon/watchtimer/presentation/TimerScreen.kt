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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.bseon.watchtimer.R


@Composable
fun TimerScreen(viewModel: MainViewModel, vibrationHelper: VibrationHelper) {
    val pickerState = rememberPickerState(60, 30)

    val timerState by viewModel.customTimerState.observeAsState(TimerState.STOPPED)
    val timeLeft by viewModel.customTimerDuration.observeAsState(MainViewModel.MIllIS_IN_FUTURE)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (timeLeft == 0L) { viewModel.onTimerAction(TimerAction.FINISH, pickerState, vibrationHelper) }

        TimerTitle()

        Spacer(modifier = Modifier.height(15.dp))

        TimerContent(timerState, pickerState, timeLeft)

        Spacer(modifier = Modifier.height(15.dp))

        TimerButton(timerState) {
            viewModel.onTimerAction(it, pickerState, vibrationHelper)
        }

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
                fontSize = 50.sp,
                text = timeLeft.toMinutes().toString()
            )
        }
    }
}

@Composable
fun TimerButton(timerState: TimerState, onActionClick: (TimerAction) -> Unit) {
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
                .clickable {
                    when(timerState) {
                        TimerState.RUNNING -> onActionClick(TimerAction.PAUSED)
                        TimerState.PAUSED -> onActionClick(TimerAction.RESUME)
                        TimerState.STOPPED -> onActionClick(TimerAction.START)
                        TimerState.FINISHED -> onActionClick(TimerAction.STOP)
                    }
                },
            contentDescription = "Run Button",
            )

        if(timerState == TimerState.PAUSED) {
            Spacer(modifier = Modifier.width(15.dp))

            Image(
                painter = painterResource(R.drawable.ic_stop_btn),
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { onActionClick(TimerAction.STOP) },
                contentDescription = "Stop Button",
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "Stopped State")
@Composable
fun WearAppPreview() {
    WearApp(
        viewModel = object : MainViewModel() {
            override val customTimerState: MutableLiveData<TimerState> = MutableLiveData(TimerState.RUNNING)
            override val customTimerDuration: MutableLiveData<Long> = MutableLiveData(60000L) // 1 minute
        },
        vibrationHelper = VibrationHelper(LocalContext.current)
    )
}