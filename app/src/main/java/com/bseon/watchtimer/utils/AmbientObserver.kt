package com.bseon.watchtimer.utils

import androidx.wear.ambient.AmbientLifecycleObserver

class AmbientObserver: AmbientLifecycleObserver.AmbientLifecycleCallback {
    override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
        // ... Called when entering ambient mode.
    }

    override fun onExitAmbient() {
        // ... Called when leaving ambient mode, back into interactive mode.
    }

    override fun onUpdateAmbient() {
        // ... Called when the display needs to be updated in ambient mode.
    }
}