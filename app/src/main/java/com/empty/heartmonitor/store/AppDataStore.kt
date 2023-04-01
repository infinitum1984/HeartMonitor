package com.empty.heartmonitor.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*

interface AppDataStore {

    val monitoringState: Flow<Boolean>

    suspend fun setIsMonitoring(isMonitoring: Boolean)

    suspend fun getIsMonitoring(): Boolean

    suspend fun setLastSendMessageDate(date: Date)

    suspend fun getLastSendMessageDate(): Date

}

class BaseAppDataStore(private val dataStore: DataStore<Preferences>) : AppDataStore {

    override val monitoringState: Flow<Boolean>
        get() = dataStore.data.map {
            it[IS_MONITORING] ?: true
        }

    override suspend fun setIsMonitoring(isMonitoring: Boolean) {
        dataStore.edit {
            it[IS_MONITORING] = isMonitoring
        }
    }

    override suspend fun getIsMonitoring(): Boolean =
        dataStore.data.first()[IS_MONITORING] ?: true

    override suspend fun setLastSendMessageDate(date: Date) {
        dataStore.edit {
            it[LAST_MESSAGE_DATE] = date.time
        }
    }

    override suspend fun getLastSendMessageDate() =
        Date(dataStore.data.first()[LAST_MESSAGE_DATE] ?: 0)

    private companion object {

        val IS_MONITORING = booleanPreferencesKey("IS_MONITORING")
        val LAST_MESSAGE_DATE = longPreferencesKey("LAST_MESSAGE_DATE")
    }

}