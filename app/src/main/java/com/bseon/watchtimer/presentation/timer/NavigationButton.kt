package com.bseon.watchtimer.presentation.timer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bseon.watchtimer.R

@Composable
fun navigationButton(modifier: Modifier, onActionClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.ic_right_arrow),
        modifier = modifier
            .size(30.dp)
            .clickable { onActionClick() },
        contentDescription = "Navigation Button",
    )
}