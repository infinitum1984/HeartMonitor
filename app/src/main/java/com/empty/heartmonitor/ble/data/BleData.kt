package com.empty.heartmonitor.ble.data


data class BleData(
    val isTouched: Boolean,
    val avgBpm: Int,
    val temperature: Double
){
    companion object{
        fun fromStr(inputRawData: String): BleData {
            val splitArray = inputRawData.split(",")
            return BleData(
                isTouched = splitArray[0].toBooleanFromBit(),
                avgBpm = splitArray[1].toInt(),
                temperature = splitArray[2].toDouble()
            )
        }
    }

}

fun String.toBooleanFromBit() =trim().equals("1")