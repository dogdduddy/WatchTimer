package com.bseon.watchtimer.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bseon.watchtimer.presentation.pickerscreen.PickerScreen
import com.bseon.watchtimer.presentation.rotaryinput.RotaryInputScreen
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel

@Composable
fun SwipeNavigation(viewModel: MainViewModel) {
    val pagerState = rememberPagerState { 2 }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 2
    ) { page ->
        when (page) {
            0 -> PickerScreen(viewModel)
            1 -> RotaryInputScreen(viewModel)
        }
    }
}
