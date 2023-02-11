package com.empty.heartmonitor.di

import com.empty.heartmonitor.ble.mapper.BleDeviceDomainUiMapper
import com.empty.heartmonitor.device.presentation.DeviceViewModel
import com.empty.heartmonitor.heart.HeartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val deviceModule = module {
    viewModel { DeviceViewModel(get(), get<BleDeviceDomainUiMapper>()) }
    viewModel { HeartViewModel(get()) }
}