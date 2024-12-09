package com.bseon.watchtimer.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun StateLessImage(
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    ) {
    val painterResource = painterResource(id = resourceId)

    Image(
        painter = painterResource,
        modifier = modifier,
        contentDescription = contentDescription,
    )
}