package com.bseon.watchtimer.utils

import androidx.wear.ambient.AmbientLifecycleObserver
import com.bseon.watchtimer.model.AmbientState

class AmbientObserver: AmbientLifecycleObserver.AmbientLifecycleCallback {
    override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
        AmbientState.isAmbient = true
    }

    override fun onExitAmbient() {
        // ... Called when leaving ambient mode, back into interactive mode.
    }

    override fun onUpdateAmbient() {
        AmbientState.isAmbient = false
    }
}