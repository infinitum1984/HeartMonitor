package com.empty.heartmonitor.di

import android.bluetooth.BluetoothManager
import com.empty.heartmonitor.ble.MyBleManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bleModule = module {
    single { MyBleManager(androidContext()) }
    single {
        androidContext().applicationContext.getSystemService(BluetoothManager::class.java).adapter.bluetoothLeScanner
    }
}