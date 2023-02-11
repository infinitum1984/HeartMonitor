package com.empty.heartmonitor.ble.mapper

import com.empty.heartmonitor.ble.data.BleData
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.core.mapper.MapperBase

class BleDataDomainMapper : MapperBase<BleData, BleDataDomain>() {
    override fun map(value: BleData) = BleDataDomain(
        bpm = value.bpm,
        avgBpm = value.avgBpm,
        isTouched = value.isTouched,
        temperature = 0
    )
}