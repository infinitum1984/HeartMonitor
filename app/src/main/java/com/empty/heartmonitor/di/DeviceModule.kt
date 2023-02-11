package com.empty.heartmonitor.di

import com.empty.heartmonitor.device.data.BaseBleRepository
import com.empty.heartmonitor.device.domain.BleRepository
import com.empty.heartmonitor.device.mapper.BleDeviceDomainUiMapper
import com.empty.heartmonitor.device.mapper.BluetoothDeviceMapper
import com.empty.heartmonitor.device.presentation.DeviceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val deviceModule = module {
    viewModel { DeviceViewModel(get(), get<BleDeviceDomainUiMapper>()) }
    factory { BluetoothDeviceMapper() }
    factory { BleDeviceDomainUiMapper() }
    single<BleRepository> { BaseBleRepository(get(), get(), get<BluetoothDeviceMapper>()) }
}