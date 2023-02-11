package com.empty.heartmonitor.device.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.device.domain.BluetoothDeviceDomain
import com.empty.heartmonitor.device.domain.BoundState

class BluetoothDeviceMapper : MapperBase<BluetoothDevice, BluetoothDeviceDomain>() {
    @SuppressLint("MissingPermission")
    override fun map(value: BluetoothDevice) = BluetoothDeviceDomain(
        value.name,
        value.address,
        when (value.bondState) {
            BluetoothDevice.BOND_BONDING -> BoundState.BOUNDING
            BluetoothDevice.BOND_BONDED -> BoundState.BOUNDED
            else -> BoundState.NONE
        }
    )
}