package com.empty.heartmonitor.messaging.net.model


data class Message(
    val to: String,
    val notification: Notification,
    val data: Data = Data(notification.title, notification.body)
)

data class Notification(
    val title: String,
    val body: String
)

data class Data(
    val title: String,
    val body: String
)