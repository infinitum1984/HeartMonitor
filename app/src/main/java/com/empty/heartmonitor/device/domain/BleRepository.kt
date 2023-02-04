package com.empty.heartmonitor.device.domain

import kotlinx.coroutines.flow.Flow


interface BleRepository {

    val connectedDevice: Flow<BluetoothDeviceDomain>

    val listNearbyDevices: Flow<List<BluetoothDeviceDomain>>

    fun startScan()

    fun stopScan()

    fun connect(deviceAdders: String)
}