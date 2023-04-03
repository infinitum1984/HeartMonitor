package com.empty.heartmonitor.tracking.data

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.core.ext.isServiceRunning
import com.empty.heartmonitor.messaging.MessagingManager
import com.empty.heartmonitor.service.TrackingService
import com.empty.heartmonitor.store.AppDataStore
import com.empty.heartmonitor.tracking.domain.TrackingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*

class BaseTrackingRepository(
    private val dataStore: AppDataStore,
    private val trackingDao: TrackingDao,
    private val context: Context,
    private val messagingManager: MessagingManager
) : TrackingRepository {

    override val watchers: Flow<List<Watcher>>
        get() = trackingDao.allWatchers()

    private val trackingStateChannel = Channel<Boolean>(Channel.BUFFERED)

    override val trackingState: Flow<Boolean>
        get() = trackingStateChannel.receiveAsFlow()

    override val monitoringState: Flow<Boolean>
        get() = dataStore.monitoringState

    init {
        trackingStateChannel.trySend(context.isServiceRunning(TrackingService::class.java))
    }

    override suspend fun addWatcher(guid: String, name: String) {
        trackingDao.addWatcher(Watcher(guid, name))
    }

    override suspend fun removeWatcher(guid: String) {
        trackingDao.removeWatcher(guid)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun changeTrackingState() {
        if (context.isServiceRunning(TrackingService::class.java)) {
            context.applicationContext.stopService(
                Intent(
                    context,
                    TrackingService::class.java
                )
            )
            trackingStateChannel.send(false)
        } else {
            context.applicationContext.startForegroundService(
                Intent(
                    context,
                    TrackingService::class.java
                )
            )
            trackingStateChannel.send(true)
        }
    }

    override suspend fun changeMonitoring(monitoring: Boolean) {
        dataStore.setIsMonitoring(monitoring)
    }

    override suspend fun sendAlertMessage(bleData: BleDataDomain) {
        for (watcher in trackingDao.getAllWatchers()) {
            try {
                messagingManager.sendMessage(
                    watcher.guid,
                    "Критичні показники від ${dataStore.getUserName()}",
                    "${bleData.avgBpm}уд/хв ${bleData.temperature}°C"
                )
                delay(200)
                dataStore.setLastSendMessageDate(Date())
            } catch (e: Exception) {
                Log.d("D_BaseTrackingRepository", "sendAlertMessage: ${e.stackTraceToString()}")
            }
        }
    }
}