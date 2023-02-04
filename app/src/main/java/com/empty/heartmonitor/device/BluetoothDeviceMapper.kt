package com.empty.heartmonitor.device

import android.bluetooth.BluetoothDevice
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.device.domain.BluetoothDeviceDomain

class BluetoothDeviceMapper : MapperBase<BluetoothDevice, BluetoothDeviceDomain>() {
    override fun map(value: BluetoothDevice) = BluetoothDeviceDomain(
        value.name
    )
}