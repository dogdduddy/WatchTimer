package com.bseon.watchtimer.presentation.rotaryinput

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.bseon.watchtimer.MockApplication
import com.bseon.watchtimer.presentation.WearApp
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel

@Composable
fun RotaryInputScreen(viewModel: MainViewModel, navController: NavController) {
    Text(text ="Rotary Input Screen")
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, name = "App Preview")
@Composable
fun RotaryPreview() {
    WearApp(MainViewModel(MockApplication()), "rotary")
}