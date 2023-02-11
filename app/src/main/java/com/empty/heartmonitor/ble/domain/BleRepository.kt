package com.empty.heartmonitor.ble.domain

import kotlinx.coroutines.flow.Flow


interface BleRepository {

    val connectedDevice: Flow<BluetoothDeviceDomain>

    val listNearbyDevices: Flow<List<BluetoothDeviceDomain>>

    val bleData: Flow<BleDataDomain>

    fun startScan()

    fun stopScan()

    suspend fun connect(deviceAdders: String)
}