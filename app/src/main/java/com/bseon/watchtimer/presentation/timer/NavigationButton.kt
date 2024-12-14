package com.bseon.watchtimer.presentation.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.bseon.watchtimer.R
import com.bseon.watchtimer.utils.StateLessImage

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    rotate: Float = 0f,
    onActionClick: () -> Unit,
) {
    StateLessImage(
        resourceId = R.drawable.ic_right_arrow,
        modifier = modifier
            .size(30.dp)
            .rotate(rotate)
            .clickable { onActionClick() },
        contentDescription = "Navigation Button",
    )
}