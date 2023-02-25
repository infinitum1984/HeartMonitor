package com.empty.heartmonitor.notification.mapper

import com.empty.heartmonitor.R
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.notification.model.NotificationHealthData

class BleDomainToNotificationData : MapperBase<BleDataDomain, NotificationHealthData>() {
    override fun map(value: BleDataDomain) = NotificationHealthData(
        bpm = value.avgBpm,
        temperature = value.temperature,
        infoText = "Показники у нормі",
        textColorRes = R.color.green,
    )
}