package com.bseon.watchtimer.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationSystem() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "picker") {
        composable("picker") {
            PickerScreen(navController)
        }
        composable("rotary") {
            RotaryInputScreen(navController)
        }
    }
}
