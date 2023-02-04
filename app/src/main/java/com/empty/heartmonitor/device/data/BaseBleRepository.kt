package com.empty.heartmonitor.device.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.empty.heartmonitor.ble.MyBleManager
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.device.domain.BleRepository
import com.empty.heartmonitor.device.domain.BluetoothDeviceDomain
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class BaseBleRepository(
    private val bleManager: MyBleManager,
    private val bleScanner: BluetoothLeScanner,
    private val mapper: MapperBase<BluetoothDevice, BluetoothDeviceDomain>
) : BleRepository {
    private val connectedDeviceChannel = Channel<BluetoothDeviceDomain>(Channel.BUFFERED)
    override val connectedDevice: Flow<BluetoothDeviceDomain>
        get() = connectedDeviceChannel.receiveAsFlow()

    private val nearbyDevicesChannel = Channel<List<BluetoothDeviceDomain>>(Channel.BUFFERED)
    override val listNearbyDevices: Flow<List<BluetoothDeviceDomain>>
        get() = nearbyDevicesChannel.receiveAsFlow()

    private val listDevices = arrayListOf<BluetoothDevice>()

    @SuppressLint("MissingPermission")
    override fun startScan() {
        bleScanner.startScan(leScanCallback)
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        bleScanner.stopScan(leScanCallback)
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            addDevice(result.device)
        }
    }

    private fun addDevice(device: BluetoothDevice) {
        listDevices.add(device)
        nearbyDevicesChannel.trySend(mapper.map(listDevices))
    }

    override fun connect(deviceAdders: String) {
        listDevices.find { it.address == deviceAdders }?.let {
            bleManager.connect(it)
            connectedDeviceChannel.trySend(mapper.map(it))
        }
    }
}