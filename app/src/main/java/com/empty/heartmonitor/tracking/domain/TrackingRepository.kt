package com.empty.heartmonitor.tracking.domain

import kotlinx.coroutines.flow.Flow

interface TrackingRepository {


    suspend fun getWatchers(): Flow<Watcher>

    suspend fun addWatcher(guid: String)

    suspend fun removeWatcher(guid: String)
}