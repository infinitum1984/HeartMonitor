package com.empty.heartmonitor.notification.mapper

import com.empty.heartmonitor.ble.domain.BleDataAnalyzer
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.notification.model.NotificationHealthData

class BleDomainToNotificationData(private val analyzer: BleDataAnalyzer) :
    MapperBase<BleDataDomain, NotificationHealthData>() {
    override fun map(value: BleDataDomain): NotificationHealthData {
        val conclusion = analyzer.analyze(value)
        return NotificationHealthData(
            bpm = value.avgBpm,
            temperature = value.temperature,
            infoText = conclusion.text,
            textColorRes = conclusion.textColor,
        )
    }
}