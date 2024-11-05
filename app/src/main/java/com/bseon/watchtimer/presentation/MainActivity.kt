/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.bseon.watchtimer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.bseon.watchtimer.presentation.theme.WatchTimerTheme
import com.example.watchtimer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val timeLeft = viewModel.customTimerDuration.observeAsState(MainViewModel.MIllIS_IN_FUTURE)
    val timerState by viewModel.customTimerState.observeAsState(MainViewModel.TimerState.STOPPED)


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
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
    Text (
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = (timeLeft.value / MainViewModel.TICK_INTERVAL).toString()
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(MainViewModel())
}

