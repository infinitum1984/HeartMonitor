package com.empty.heartmonitor.ble.domain

interface BleDataAnalyzer {

    fun analyze(bleData: BleDataDomain): Conclusion
}

