package com.bseon.watchtimer.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
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
import com.bseon.watchtimer.R


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

        Spacer(modifier = Modifier.height(15.dp))

        Box {
            navigationButton(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { navController.navigate("rotary") }
            TimerContent(timerState, pickerState, timeLeft)
        }

        Spacer(modifier = Modifier.height(15.dp))

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
fun TimerTitle() {
    Text(text = "Minute")
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
                text = timeLeft.toString()
            )
        }
    }
}

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

@Composable
fun navigationButton(modifier: Modifier, onActionClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.ic_right_arrow),
        modifier = modifier
            .size(30.dp)
            .clickable { onActionClick() },
        contentDescription = "Navigation Button",
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "App Preview")
@Composable
fun PickerPreview() {
    WearApp()
}