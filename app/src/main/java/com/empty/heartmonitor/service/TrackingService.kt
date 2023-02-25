package com.empty.heartmonitor.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.ble.domain.BleRepository
import com.empty.heartmonitor.notification.TrackingNotificationManager
import com.empty.heartmonitor.notification.mapper.BleDomainToNotificationData
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject


class TrackingService : Service() {

    private val bleRepository: BleRepository by inject()

    private val trackingNotificationManager: TrackingNotificationManager by inject()

    private val mapper: BleDomainToNotificationData by inject()


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        trackingNotificationManager.showHealthInfo(mapper.map(BleDataDomain(0, 0.0, false)))

        bleRepository.bleData.onEach {
            trackingNotificationManager.showHealthInfo(mapper.map(it))
        }
    }
}