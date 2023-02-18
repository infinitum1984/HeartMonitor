package com.empty.heartmonitor.ble.domain

data class BleDataDomain(
    val avgBpm: Int,
    val temperature: Double,
    val isTouched: Boolean,
)