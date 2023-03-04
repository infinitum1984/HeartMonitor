package com.empty.heartmonitor.notification

import android.app.Service
import com.empty.heartmonitor.notification.model.NotificationHealthData

interface TrackingNotificationManager {
    fun bindForeground(service: Service)
    fun showHealthInfo(data: NotificationHealthData)

    fun hide()
}