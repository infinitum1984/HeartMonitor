package com.empty.heartmonitor.messaging

import android.util.Log
import com.empty.heartmonitor.notification.TrackingNotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class HmFirebaseMessagingService : FirebaseMessagingService() {
    private val trackingNotificationManager: TrackingNotificationManager by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("D_HmFirebaseMessagingService", "onMessageReceived: ${message.data}")
        message.data.let {
            trackingNotificationManager.showPushNotification(it["title"] ?: "", it["body"] ?: "")
        }
    }
}