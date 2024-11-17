/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.bseon.watchtimer.presentation

import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.rotary.RotaryScrollableBehavior
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import com.bseon.watchtimer.presentation.theme.WatchTimerTheme
import com.bseon.watchtimer.R

class MainActivity : ComponentActivity() {
    private lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel()
        setContent {
            WearApp(viewModel)
        }

    }
}

@Composable
fun WearApp(viewModel: MainViewModel) {
    WatchTimerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center
        ) {
            TimerScreen(viewModel)
        }
    }
}

@Composable
fun TimerScreen(viewModel: MainViewModel) {
    val pickerState = rememberPickerState(60, 30)

    val timerState by viewModel.customTimerState.observeAsState(MainViewModel.TimerState.STOPPED)
    val timeLeft by viewModel.customTimerDuration.observeAsState(MainViewModel.MIllIS_IN_FUTURE)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Minute")

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (timerState == MainViewModel.TimerState.STOPPED) {
                Picker(
                    state = pickerState,
                    contentDescription = "Number Picker",
                    separation = 1.dp,) {
                    Text(text = "$it", fontSize = 24.sp)
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

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),
            onClick = {

                // ViewModel에서 값을 postValue로 반영하는데, 데이터 반영 전에 초기 값으로 작업이 실행 되는 경우는 없을지 확인이 필요하다.
                viewModel.setTimerDuration(pickerState.selectedOption.toMillis())

                when(timerState) {
                    MainViewModel.TimerState.RUNNING -> viewModel.pauseTimer()
                    MainViewModel.TimerState.PAUSED -> viewModel.resumeTimer()
                    MainViewModel.TimerState.STOPPED -> viewModel.startTimer()
                }
            }) {
            Text(text = when(timerState) {
                MainViewModel.TimerState.RUNNING -> stringResource(id = R.string.state_pause)
                MainViewModel.TimerState.PAUSED -> stringResource(id = R.string.state_resume)
                MainViewModel.TimerState.STOPPED -> stringResource(id = R.string.state_start)
            })
        }
        if(timerState == MainViewModel.TimerState.PAUSED) {
            Button(
                onClick = {
                    viewModel.stopTimer()
                }) {
                Text(text = "Stop")
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(MainViewModel())
}


fun Int.toMillis(): Long {
    return this.toLong() * 60 * 1000
}

fun Long.toMinutes(): Int {
    return (this / 1000 / 60).toInt()
}
