package com.bseon.watchtimer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bseon.watchtimer.presentation.pickerscreen.PickerScreen
import com.bseon.watchtimer.presentation.rotaryinput.RotaryInputScreen
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel

@Composable
fun NavigationSystem(viewModel: MainViewModel, startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("picker") {
            PickerScreen(viewModel, navController)
        }
        composable("rotary") {
            RotaryInputScreen(viewModel, navController)
        }
    }
}
