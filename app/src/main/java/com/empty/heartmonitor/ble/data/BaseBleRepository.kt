package com.empty.heartmonitor.ble.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.ble.domain.BleRepository
import com.empty.heartmonitor.ble.domain.BluetoothDeviceDomain
import com.empty.heartmonitor.core.mapper.MapperBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import no.nordicsemi.android.ble.PhyRequest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BaseBleRepository(
    private val bleManager: MyBleManager,
    private val bleScanner: BluetoothLeScanner,
    private val mapper: MapperBase<BluetoothDevice, BluetoothDeviceDomain>,
    private val mapperData: MapperBase<BleData, BleDataDomain>

) : BleRepository {
    private val connectedDeviceChannel = Channel<BluetoothDeviceDomain>(Channel.BUFFERED)
    override val connectedDevice: Flow<BluetoothDeviceDomain>
        get() = connectedDeviceChannel.receiveAsFlow()

    private val nearbyDevicesChannel = Channel<List<BluetoothDeviceDomain>>(Channel.BUFFERED)
    override val listNearbyDevices: Flow<List<BluetoothDeviceDomain>>
        get() = nearbyDevicesChannel.receiveAsFlow()
    override val bleData: Flow<BleDataDomain>
        get() = bleManager.bleDataFlow.map { mapperData.map(it) }

    private val listDevices = arrayListOf<BluetoothDevice>()

    @SuppressLint("MissingPermission")
    override fun startScan() {
        Log.d("BaseBleRepository", "startScan: ")
        bleScanner.startScan(leScanCallback)
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        bleScanner.stopScan(leScanCallback)
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("BaseBleRepository","onScanResult: ${result.device.name} ${result.device.bondState}")
            addDevice(result.device)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addDevice(device: BluetoothDevice) {
        if (device.bondState == BluetoothDevice.BOND_BONDED){
            connectedDeviceChannel.trySend(mapper.map(device))
        }
        if (listDevices.firstOrNull { it.address == device.address } == null) {
            listDevices.add(device)
            nearbyDevicesChannel.trySend(mapper.map(listDevices))
        }
    }

    override suspend fun connect(deviceAdders: String)  =
        suspendCoroutine<Unit> { cont ->
            listDevices.find { it.address == deviceAdders }?.let {
                bleManager.connect(it) // Automatic retries are supported, in case of 133 error.
                    .retry(
                        3 /* times, with */,
                        100 /* ms interval */
                    ) // A connection timeout can be set. This is additional to the Android's connection timeout which is 30 seconds.
                    .timeout(15000 /* ms */) // The auto connect feature from connectGatt is available as well
                    .useAutoConnect(true) // This API can be set on any Android version, but will only be used on devices running Android 8+ with
                    // support to the selected PHY.
                    .usePreferredPhy(PhyRequest.PHY_LE_1M_MASK or PhyRequest.PHY_LE_2M_MASK or PhyRequest.PHY_LE_CODED_MASK) // A connection timeout can be also set. This is additional to the Android's connection timeout which is 30 seconds.
                    .timeout(15000 /* ms */) // Each request has number of callbacks called in different situations:
                    .before { device -> }
                    .done { device ->
                        Log.d("BleManagerFragment", ": done ")
                        connectedDeviceChannel.trySend(mapper.map(device))
                        cont.resume(Unit)
                    }
                    .fail { device, status -> Log.d("BleManagerFragment", ": fail ${status} ")
                        cont.resumeWithException(Exception("${status}"))
                    }
                    .enqueue()
            }

        }

}