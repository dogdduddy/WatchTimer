/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.bseon.watchtimer.presentation

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.ambient.AmbientLifecycleObserver
import androidx.wear.compose.material.MaterialTheme
import com.bseon.watchtimer.MockApplication
import com.bseon.watchtimer.navigation.NavigationSystem
import com.bseon.watchtimer.presentation.theme.WatchTimerTheme
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import com.bseon.watchtimer.utils.AmbientObserver

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val ambientCallback = AmbientObserver()
    private val ambientObserver = AmbientLifecycleObserver(this, ambientCallback)

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(ambientObserver)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WatchTimer::WakeLock")
        wakeLock.acquire()

        setContent {
            WearApp(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(ambientObserver)
        if (::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}

@Composable
fun WearApp(viewModel: MainViewModel, startDestination: String = "picker") {
    WatchTimerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            NavigationSystem(viewModel, startDestination)
        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(MainViewModel(MockApplication()))
}