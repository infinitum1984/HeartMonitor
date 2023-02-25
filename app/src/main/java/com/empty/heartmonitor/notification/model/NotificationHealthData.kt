package com.empty.heartmonitor.notification.model

data class NotificationHealthData(
    val bpm: Int,
    val temperature: Double,
    val infoText: String,
    val textColorRes: Int
)