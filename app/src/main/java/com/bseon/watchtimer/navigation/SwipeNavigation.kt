package com.bseon.watchtimer.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.bseon.watchtimer.presentation.pickerscreen.PickerScreen
import com.bseon.watchtimer.presentation.rotaryinput.RotaryInputScreen
import com.bseon.watchtimer.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SwipeNavigation(viewModel: MainViewModel) {
    val pagerState = rememberPagerState { 2 }

    val coroutineScope = rememberCoroutineScope()
    val onNavigateToNextPage: () -> Unit = {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            withContext(coroutineScope.coroutineContext) {
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 2
    ) { page ->
        when (page) {
            0 -> PickerScreen(viewModel, onNavigateToNextPage)
            1 -> RotaryInputScreen(viewModel, onNavigateToNextPage)
        }
    }
}
