package com.empty.heartmonitor.notification

import com.empty.heartmonitor.notification.model.NotificationHealthData

interface TrackingNotificationManager {

    fun showHealthInfo(data: NotificationHealthData)

    fun hide()
}