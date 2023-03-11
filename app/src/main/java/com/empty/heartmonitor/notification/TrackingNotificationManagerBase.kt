package com.empty.heartmonitor.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.empty.heartmonitor.BuildConfig
import com.empty.heartmonitor.R
import com.empty.heartmonitor.notification.model.NotificationHealthData

class TrackingNotificationManagerBase(private val context: Context) : TrackingNotificationManager {
    companion object {
        private const val CHANNEL_ID = "0"
        private const val CHANNEL_ID_PUSH = "1"

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_PUSH = 2

    }

    private val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            notificationManager.createNotificationChannel(mChannel)
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID_PUSH,
                    context.getString(R.string.channel_name) + "PUSH",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    override fun bindForeground(service: Service) {
        service.startForeground(
            NOTIFICATION_ID,
            getNotification(NotificationHealthData(0, 0.0, "", R.color.normalTextColor))
        )
    }

    @SuppressLint("RemoteViewLayout")
    override fun showHealthInfo(data: NotificationHealthData) {
        notificationManager.notify(NOTIFICATION_ID, getNotification(data))
    }

    @SuppressLint("RemoteViewLayout")
    private fun getNotification(data: NotificationHealthData): Notification {
        val notificationLayout =
            RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_small)
        notificationLayout.setTextViewText(R.id.bpm, "${data.bpm}уд/хв")
        notificationLayout.setTextViewText(R.id.temp, "${data.temperature}°C")
        notificationLayout.setTextColor(
            R.id.infoText,
            context.resources.getColor(data.textColorRes)
        )
        notificationLayout.setTextViewText(R.id.infoText, data.infoText)
        val customNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .build()
        return customNotification
    }

    override fun hideHealthInfo() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    override fun showPushNotification(title: String, text: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_PUSH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentTitle(title)
            .setContentText(text)
            .build()
        notificationManager.notify(NOTIFICATION_PUSH, notification)
    }

}