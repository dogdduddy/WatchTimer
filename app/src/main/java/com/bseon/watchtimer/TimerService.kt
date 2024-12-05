package com.bseon.watchtimer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bseon.watchtimer.model.TimerIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    private var timeDuration: Int = 0

    private var timerJob: Job? = null

    private lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        when (parseTimerIntent(intent)) {
            TimerIntent.TimerStartedIntent -> {
                val duration = intent?.getIntExtra(TIME_DURATION, 0) ?: 0
                startTimer(duration)
            }
            TimerIntent.TimerPausedIntent -> pauseTimer()
            TimerIntent.TimerResumedIntent -> resumeTimer()
            TimerIntent.TimerCancelledIntent -> cancelTimer()
            TimerIntent.TimerFinishedIntent -> finishTimer()
            else -> {
                // Do nothing
            }
        }
        return START_STICKY
    }

    private fun startTimer(duration: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, buildNotification())
        }
        timeDuration = duration

        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (timeDuration > 0) {
                delay(MINUTES_TICK_INTERVAL)
                timeDuration--

                val timerIntent = Intent()
                timerIntent.action = TIMER_TICK
                timerIntent.putExtra(TIME_DURATION, timeDuration)
                sendBroadcast(timerIntent)

                updateNotification()
            }
            if (isActive) {
                finishTimer() // 타이머 완료 시 서비스 종료
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resumeTimer() {
        startTimer(timeDuration)
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        stopForeground(true)
    }

    private fun finishTimer() {
        timerJob?.cancel()
        stopForeground(true)

        val timerIntent = Intent()
        timerIntent.action = TIMER_FINISHED
        sendBroadcast(timerIntent)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "TIMER",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun buildNotification(): Notification {
        // 알림 채널 생성 (Android 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 빌드
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText("Timer is currently active.")
            .setSmallIcon(R.drawable.ic_run_btn) // 알림 아이콘
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        timerJob?.cancel()
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        timerJob?.cancel()
        stopForeground(true)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "Timer_Notifications"

        const val MIllIS_IN_FUTURE = 1000 * 60  * 30L
        const val MINUTES_TICK_INTERVAL = 1000 * 60L

        // Intent Extras
        const val TIME_DURATION = "TIME_DURATION"

        // Intent Actions
        const val TIMER_TICK = "TIMER_TICK"
        const val TIMER_STARTED = "TIMER_STARTED"
        const val TIMER_PAUSED = "TIMER_PAUSED"
        const val TIMER_RESUMED = "TIMER_RESUMED"
        const val TIMER_CANCELLED = "TIMER_CANCELLED"
        const val TIMER_FINISHED = "TIMER_FINISHED"

        fun parseTimerIntent(intent: Intent?): TimerIntent? {
            return when (intent?.action) {
                TIMER_STARTED -> TimerIntent.TimerStartedIntent
                TIMER_PAUSED -> TimerIntent.TimerPausedIntent
                TIMER_RESUMED -> TimerIntent.TimerResumedIntent
                TIMER_CANCELLED -> TimerIntent.TimerCancelledIntent
                TIMER_FINISHED -> TimerIntent.TimerFinishedIntent
                else -> null
            }
        }
    }
}