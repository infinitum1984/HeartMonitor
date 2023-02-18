package com.empty.heartmonitor.temperature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empty.heartmonitor.ble.domain.BleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TemperatureViewModel(private val bleRepository: BleRepository) : ViewModel() {

    private val _temperature = MutableStateFlow(0.0)
    val temperature = _temperature.asStateFlow()

    private val _measuredTemperature = MutableStateFlow(0.0)
    val measuredTemperature = _measuredTemperature.asStateFlow()

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring = _isMeasuring.asStateFlow()


    private val measuredArray = arrayListOf<Double>()

    init {
        bleRepository.bleData.onEach {
            _temperature.emit(it.temperature)
            if (isMeasuring.value)
                measuredArray.add(it.temperature)
        }.launchIn(viewModelScope)
    }

    fun startMeasure() {
        if (!isMeasuring.value)
            viewModelScope.launch(Dispatchers.IO) {
                _isMeasuring.emit(true)
                delay(30000)
                _isMeasuring.emit(false)
                Log.d("HeartViewModel", "${measuredArray.size}")
                Log.d("HeartViewModel", "${measuredArray}")

                _measuredTemperature.emit(measuredArray.average())
                measuredArray.clear()
            }
    }
}

fun List<Int>.average(): Double {
    var sum = 0.0
    forEach {
        sum += it
    }
    return sum / size
}