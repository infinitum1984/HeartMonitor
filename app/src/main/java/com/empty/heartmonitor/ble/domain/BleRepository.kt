package com.empty.heartmonitor.ble.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface BleRepository {

    val connectedDevice: StateFlow<BluetoothDeviceDomain?>

    val listNearbyDevices: StateFlow<List<BluetoothDeviceDomain>>

    val bleData: Flow<BleDataDomain>

    fun startScan()

    fun stopScan()

    suspend fun connect(deviceAdders: String)
    suspend fun disconnect()
}