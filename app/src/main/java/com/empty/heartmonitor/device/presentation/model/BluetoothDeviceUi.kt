package com.empty.heartmonitor.device.presentation.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class BluetoothDeviceUi(
    private val name:String,
    private val macAddress: String
){

    @Composable
    fun show(onClickAction:(mac: String)-> Unit = {}){
        Row(modifier = Modifier.padding(8.dp)) {
            Image(imageVector = Icons.Default.Phone, contentDescription = "device", modifier = Modifier.padding(4.dp))
            Column(Modifier.padding(end = 4.dp)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(macAddress)
            }
            Button(modifier = Modifier.padding(4.dp), onClick = { onClickAction.invoke(macAddress) }) {
                Text(text = "Підключитись")
            }
        }
    }
}