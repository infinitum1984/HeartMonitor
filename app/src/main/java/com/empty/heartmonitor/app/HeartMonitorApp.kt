package com.empty.heartmonitor.app

import android.app.Application
import com.empty.heartmonitor.di.bleModule
import com.empty.heartmonitor.di.deviceModule
import com.empty.heartmonitor.di.messagingModule
import com.empty.heartmonitor.di.trackingModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HeartMonitorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(deviceModule, bleModule, trackingModule, messagingModule))
        }
    }
}