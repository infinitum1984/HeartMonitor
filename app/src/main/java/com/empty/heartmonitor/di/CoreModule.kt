package com.empty.heartmonitor.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.empty.heartmonitor.core.data.AppDatabase
import com.empty.heartmonitor.profile.ProfileViewModel
import com.empty.heartmonitor.store.AppDataStore
import com.empty.heartmonitor.store.BaseAppDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "heart-monitor-db"
        ).build()
    }
    factory { get<AppDatabase>().trackingDao() }
    factory<AppDataStore> {
        BaseAppDataStore(androidContext().dataStore)
    }
    viewModel { ProfileViewModel(get()) }
}
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore")
