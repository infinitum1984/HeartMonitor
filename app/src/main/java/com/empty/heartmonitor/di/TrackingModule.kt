package com.empty.heartmonitor.di

import com.empty.heartmonitor.notification.TrackingNotificationManager
import com.empty.heartmonitor.notification.TrackingNotificationManagerBase
import com.empty.heartmonitor.notification.mapper.BleDomainToNotificationData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val trackingModule = module {
    single<TrackingNotificationManager> { TrackingNotificationManagerBase(androidContext()) }
    single { BleDomainToNotificationData(get()) }
}