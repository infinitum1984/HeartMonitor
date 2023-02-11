package com.empty.heartmonitor.device.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empty.heartmonitor.core.mapper.MapperBase
import com.empty.heartmonitor.device.domain.BleRepository
import com.empty.heartmonitor.device.domain.BluetoothDeviceDomain
import com.empty.heartmonitor.device.presentation.model.BluetoothDeviceUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DeviceViewModel(private val bleRepository: BleRepository,
                    private val mapper: MapperBase<BluetoothDeviceDomain, BluetoothDeviceUi>
                      ) : ViewModel() {

    private val _listDevices= MutableStateFlow<List<BluetoothDeviceUi>>(listOf())
    val listDevices = _listDevices.asStateFlow()

    private val _connectedDevice = MutableStateFlow<BluetoothDeviceUi?>(null)
    val connectedDevice = _connectedDevice.asStateFlow()
    init {
        bleRepository.listNearbyDevices.onEach {
            _listDevices.emit(mapper.map(it))
        }.launchIn(viewModelScope)
        bleRepository.connectedDevice.onEach {
            _connectedDevice.emit(mapper.map(it))
        }.launchIn(viewModelScope)
    }

    fun startScanning() {
        bleRepository.startScan()
    }

    fun connectToDevice(device: String) {
        viewModelScope.launch {
            bleRepository.connect(device)
        }
    }

    fun stopScanning(){
        bleRepository.stopScan()
    }
}