package com.empty.heartmonitor.notification

import android.app.Service
import com.empty.heartmonitor.notification.model.NotificationHealthData

interface TrackingNotificationManager {
    fun bindForeground(service: Service)
    fun showHealthInfo(data: NotificationHealthData)

    fun hideHealthInfo()

    fun showPushNotification(title: String, text: String)
}