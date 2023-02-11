package com.empty.heartmonitor.device.domain

import com.empty.heartmonitor.device.data.ConnectResult
import kotlinx.coroutines.flow.Flow


interface BleRepository {

    val connectedDevice: Flow<BluetoothDeviceDomain>

    val listNearbyDevices: Flow<List<BluetoothDeviceDomain>>

    fun startScan()

    fun stopScan()

    suspend fun connect(deviceAdders: String)
}