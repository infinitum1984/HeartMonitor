package com.empty.heartmonitor.device.presentation.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empty.heartmonitor.R

data class BluetoothDeviceUi(
    private val name: String,
    private val macAddress: String,
) {

    @Composable
    fun show(connected: Boolean = false, onClickAction: (mac: String) -> Unit = {}) {
        Card(elevation = 8.dp, modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Row(modifier = Modifier.padding(8.dp)) {
                Image(
                    painter = painterResource(R.drawable.devices),
                    contentDescription = "device",
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically),
                    colorFilter = ColorFilter.tint(if (connected) colorResource(id = R.color.green) else Color.Black)
                )
                Column(
                    Modifier
                        .padding(end = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(macAddress)
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterVertically),
                    onClick = { onClickAction.invoke(macAddress) }) {
                    Text(
                        text = if (connected) "Відключитись" else "Підключитись",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

            }
        }
    }
}