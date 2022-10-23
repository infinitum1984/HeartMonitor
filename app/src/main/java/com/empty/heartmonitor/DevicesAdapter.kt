package com.empty.heartmonitor

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.empty.heartmonitor.databinding.DeviceItemBinding

class DevicesAdapter(private val onClickAction: (device: BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder>() {

    var listDevices = listOf<BluetoothDevice>()

    fun setData(devices: List<BluetoothDevice>) {
        listDevices = devices
        notifyDataSetChanged()
    }

    inner class DevicesViewHolder(private val binding: DeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BluetoothDevice) {
            binding.tvDeviceName.text = device.name ?: "none"
            binding.btConnect.setOnClickListener { onClickAction.invoke(device) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesViewHolder {
        val binding = DeviceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DevicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DevicesViewHolder, position: Int) =
        holder.bind(listDevices[position])

    override fun getItemCount() = listDevices.size
}