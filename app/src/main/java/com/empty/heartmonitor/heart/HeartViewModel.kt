package com.empty.heartmonitor.heart

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
import kotlin.math.roundToInt

class HeartViewModel(private val bleRepository: BleRepository) : ViewModel() {

    private val _heartBpm = MutableStateFlow(0)
    val heartBpm = _heartBpm.asStateFlow()

    private val _measuredBpm = MutableStateFlow(0)
    val measuredBpm = _measuredBpm.asStateFlow()

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring = _isMeasuring.asStateFlow()


    private val measuredArray = arrayListOf<Double>()

    init {
        bleRepository.bleData.onEach {
            _heartBpm.emit(it.avgBpm)
            if (isMeasuring.value)
                measuredArray.add(it.bpm)
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

                _measuredBpm.emit(measuredArray.average().roundToInt())
                measuredArray.clear()
            }
    }
}

fun List<Double>.average(): Double {
    var sum = 0.0
    forEach {
        sum += it
    }
    return sum / size
}