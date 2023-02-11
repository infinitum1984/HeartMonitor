package com.empty.heartmonitor.device.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.empty.heartmonitor.device.presentation.model.BluetoothDeviceUi

@Composable
fun DeviceScreen(deviceFragment: DeviceFragment){
    val listDevices by deviceFragment.listNearbyDevices.collectAsState()
    val connectedDevice by deviceFragment.connectedDevice.collectAsState()
    DeviceScreenUi(listDevices, connectedDevice) {
        deviceFragment.connectToDevice(it)
    }
}

@Composable
fun DeviceScreenUi(nearbyDevices: List<BluetoothDeviceUi>,connectedDeviceUi: BluetoothDeviceUi? = null,  onDeviceClickAction: (mac: String)->Unit){
    Column {
        connectedDeviceUi?.show()
        LazyColumn {
            items(nearbyDevices){device->
                device.show(onClickAction = {
                    onDeviceClickAction(it)
                })
            }
        }
    }

}