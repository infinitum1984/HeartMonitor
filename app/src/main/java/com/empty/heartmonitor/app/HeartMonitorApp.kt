package com.empty.heartmonitor.app

import android.app.Application
import com.empty.heartmonitor.di.bleModule
import com.empty.heartmonitor.di.deviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HeartMonitorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(deviceModule, bleModule))
        }
    }
}