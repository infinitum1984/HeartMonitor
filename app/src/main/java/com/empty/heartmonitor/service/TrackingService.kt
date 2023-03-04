package com.empty.heartmonitor.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.ble.domain.BleRepository
import com.empty.heartmonitor.notification.TrackingNotificationManager
import com.empty.heartmonitor.notification.mapper.BleDomainToNotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject


class TrackingService : Service() {

    private val bleRepository: BleRepository by inject()

    private val trackingNotificationManager: TrackingNotificationManager by inject()

    private val mapper: BleDomainToNotificationData by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
        trackingNotificationManager.bindForeground(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("D_TrackingService", "onStartCommand: start service")
        trackingNotificationManager.showHealthInfo(mapper.map(BleDataDomain(60, 45.0, false)))

        bleRepository.bleData.onEach {
            Log.d("D_TrackingService", "on each data ${it}")
            trackingNotificationManager.showHealthInfo(mapper.map(it))
        }.launchIn(serviceScope)


        return START_STICKY
    }
}