package com.empty.heartmonitor.ble.domain

data class BluetoothDeviceDomain(
    val name: String,
    val macAddress: String,
    val boundState: BoundState
    )

enum class BoundState{
    NONE,
    BOUNDING,
    BOUNDED
}