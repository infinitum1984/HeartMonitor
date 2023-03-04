package com.empty.heartmonitor.ble.data

import android.content.Context
import com.empty.heartmonitor.R
import com.empty.heartmonitor.ble.domain.BleDataAnalyzer
import com.empty.heartmonitor.ble.domain.BleDataDomain
import com.empty.heartmonitor.ble.domain.Conclusion

class BleDataAnalyzerBase(private val context: Context) : BleDataAnalyzer {
    override fun analyze(bleData: BleDataDomain): Conclusion {
        if (!bleData.isTouched)
            return Conclusion(context.getString(R.string.connectDevice), R.color.red)
        if (bleData.avgBpm < 60)
            return Conclusion(context.getString(R.string.lowBpm), R.color.red)
        if (bleData.avgBpm > 120)
            return Conclusion(context.getString(R.string.highBpm), R.color.red)
        return Conclusion(context.getString(R.string.normalBpm), R.color.normalTextColor)
    }
}