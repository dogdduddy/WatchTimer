package com.bseon.watchtimer.utils

import androidx.wear.ambient.AmbientLifecycleObserver

class AmbientObserver: AmbientLifecycleObserver.AmbientLifecycleCallback {
    override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
        // ... Called when moving from interactive mode into ambient mode.
    }

    override fun onExitAmbient() {
        // ... Called when leaving ambient mode, back into interactive mode.
    }

    override fun onUpdateAmbient() {
        // ... Called by the system in order to allow the app to periodically
        // update the display while in ambient mode. Typically the system will
        // call this every 60 seconds.
    }
}