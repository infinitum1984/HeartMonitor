package com.empty.heartmonitor.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.empty.heartmonitor.tracking.data.TrackingDao
import com.empty.heartmonitor.tracking.data.Watcher

@Database(entities = [Watcher::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackingDao(): TrackingDao
}