package com.empty.heartmonitor.device.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.empty.heartmonitor.device.domain.BleRepository

class DeviceViewModel(private val bleRepository: BleRepository) : ViewModel() {
    fun startScanning() {

    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}