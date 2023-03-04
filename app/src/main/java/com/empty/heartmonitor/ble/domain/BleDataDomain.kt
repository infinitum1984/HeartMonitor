package com.empty.heartmonitor.ble.domain

import kotlin.math.roundToInt

data class BleDataDomain(
    val avgBpm: Int,
    val temperature: Double,
    val isTouched: Boolean,
)

fun List<BleDataDomain>.average(): BleDataDomain {
    val avgBpm: Int = map { it.avgBpm }.average().roundToInt()
    val avgTemp: Double = map { it.temperature }.average()
    return BleDataDomain(avgBpm, avgTemp, true)
}