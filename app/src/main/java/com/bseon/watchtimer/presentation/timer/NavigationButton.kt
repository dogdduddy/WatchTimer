package com.bseon.watchtimer.presentation.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val combinedModifier = remember(modifier) {
        modifier
            .size(30.dp)
            .rotate(rotate)
            .clickable { onActionClick() }
    }

    StateLessImage(
        resourceId = R.drawable.ic_right_arrow,
        modifier = combinedModifier,
        contentDescription = "Navigation Button",
    )
}