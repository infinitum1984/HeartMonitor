package com.empty.heartmonitor.ble.domain

data class BleDataDomain(
    val bpm: Double,
    val avgBpm: Int,
    val temperature: Int,
    val isTouched: Boolean,
)