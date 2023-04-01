package com.empty.heartmonitor.tracking.domain

import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.tracking.data.Watcher
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {

    val watchers: Flow<List<Watcher>>

    val trackingState: Flow<Boolean>

    val monitoringState: Flow<Boolean>
    suspend fun addWatcher(guid: String, name: String)
    suspend fun removeWatcher(guid: String)
    suspend fun changeTrackingState()
    suspend fun changeMonitoring(monitoring: Boolean)

    suspend fun sendAlertMessage(bleData: BleDataDomain)
}