package com.empty.heartmonitor.di

import android.bluetooth.BluetoothManager
import com.empty.heartmonitor.ble.data.BaseBleRepository
import com.empty.heartmonitor.ble.data.MyBleManager
import com.empty.heartmonitor.ble.domain.BleRepository
import com.empty.heartmonitor.ble.mapper.BleDataDomainMapper
import com.empty.heartmonitor.ble.mapper.BleDeviceDomainUiMapper
import com.empty.heartmonitor.ble.mapper.BluetoothDeviceMapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bleModule = module {
    single { MyBleManager(androidContext()) }
    single {
        androidContext().applicationContext.getSystemService(BluetoothManager::class.java).adapter.bluetoothLeScanner
    }
    factory { BluetoothDeviceMapper() }
    factory { BleDeviceDomainUiMapper() }
    factory { BleDataDomainMapper() }
    single<BleRepository> {
        BaseBleRepository(
            get(),
            get(),
            get<BluetoothDeviceMapper>(),
            get<BleDataDomainMapper>()
        )
    }

}