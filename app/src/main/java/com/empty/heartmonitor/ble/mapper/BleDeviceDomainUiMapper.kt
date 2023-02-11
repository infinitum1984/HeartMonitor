package com.empty.heartmonitor.ble.mapper

import com.empty.heartmonitor.ble.domain.BluetoothDeviceDomain
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.device.presentation.model.BluetoothDeviceUi

class BleDeviceDomainUiMapper: MapperBase<BluetoothDeviceDomain, BluetoothDeviceUi>() {
    override fun map(value: BluetoothDeviceDomain)= BluetoothDeviceUi(
        name = value.name,
        macAddress = value.macAddress
    )
}