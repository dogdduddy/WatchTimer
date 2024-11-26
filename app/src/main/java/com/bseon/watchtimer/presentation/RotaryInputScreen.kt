package com.bseon.watchtimer.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun RotaryInputScreen(navController: NavController) {
    val viewModel = hiltViewModel<MainViewModel>()

}