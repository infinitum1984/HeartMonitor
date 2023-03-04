package com.empty.heartmonitor.device.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.empty.heartmonitor.device.presentation.model.BluetoothDeviceUi

@Composable
fun DeviceScreen(deviceFragment: DeviceFragment) {
    val listDevices by deviceFragment.listNearbyDevices.collectAsState()
    val connectedDevice by deviceFragment.connectedDevice.collectAsState()
    DeviceScreenUi(listDevices, connectedDevice, {
        deviceFragment.disconnect()
    }) {
        deviceFragment.connectToDevice(it)
    }
}

@Composable
fun DeviceScreenUi(
    nearbyDevices: List<BluetoothDeviceUi>,
    connectedDeviceUi: BluetoothDeviceUi? = null,
    disconnectAction: () -> Unit,
    onDeviceClickAction: (mac: String) -> Unit
) {
    Column {
        connectedDeviceUi?.let {
            Text(text = "Підключений пристрій:", Modifier.padding(4.dp), fontSize = 18.sp)
            it.show(true) {
                disconnectAction.invoke()
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "Список пристроїв:", Modifier.padding(4.dp), fontSize = 18.sp)
        Spacer(modifier = Modifier.size(4.dp))
        LazyColumn {
            items(nearbyDevices) { device ->
                device.show(onClickAction = {
                    onDeviceClickAction(it)
                })
            }
        }
    }

}