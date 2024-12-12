package com.bseon.watchtimer.presentation.rotaryinput

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.bseon.watchtimer.TimerService
import com.bseon.watchtimer.TimerService.Companion.MAX_TIMER_DURATION
import com.bseon.watchtimer.presentation.theme.TartOrange
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import com.bseon.watchtimer.utils.calculateProgress
import com.bseon.watchtimer.utils.toMinutes

@Composable
fun RotaryInputScreen(viewModel: MainViewModel) {

    val timeLeft by viewModel.customTimerDuration.observeAsState(TimerService.DEFAULT_TIMER_DURATION.toMinutes())

    Box{
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = calculateProgress(
                currentTimerValue = timeLeft,
                maxTimerValue = MAX_TIMER_DURATION.toMinutes()
            ),
            indicatorColor = TartOrange
        )

        Text(
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            fontSize = 50.sp,
            text = timeLeft.toString()
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "App Preview")
@Composable
fun RotaryPreview() {
    RotaryInputScreen(MainViewModel(LocalContext.current))
}