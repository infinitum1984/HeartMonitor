package com.empty.heartmonitor.di

import com.empty.heartmonitor.notification.TrackingNotificationManager
import com.empty.heartmonitor.notification.TrackingNotificationManagerBase
import com.empty.heartmonitor.notification.mapper.BleDomainToNotificationData
import com.empty.heartmonitor.tracking.data.BaseTrackingRepository
import com.empty.heartmonitor.tracking.domain.TrackingRepository
import com.empty.heartmonitor.tracking.presentation.TrackingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackingModule = module {
    single<TrackingNotificationManager> { TrackingNotificationManagerBase(androidContext()) }
    single { BleDomainToNotificationData(get()) }
    single<TrackingRepository> { BaseTrackingRepository(get(), get(), androidContext(), get()) }
    viewModel { TrackingViewModel(get()) }
}