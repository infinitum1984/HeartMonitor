package com.empty.heartmonitor.ble.data


data class BleData(
    val bpm: Double,
    val avgBpm: Int,
    val isTouched: Boolean,
){
    companion object{
        fun fromStr(inputRawData: String): BleData {
            val splitArray = inputRawData.split(",")
            return BleData(
                bpm = splitArray[0].toDouble(),
                avgBpm = splitArray[1].toInt(),
                isTouched = splitArray[2].toBooleanFromBit()
            )
        }
    }

}

fun String.toBooleanFromBit() =trim().equals("1")