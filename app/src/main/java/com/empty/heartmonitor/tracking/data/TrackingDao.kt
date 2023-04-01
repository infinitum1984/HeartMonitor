package com.empty.heartmonitor.tracking.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingDao {

    @Query("""SELECT * FROM Watcher""")
    fun allWatchers(): Flow<List<Watcher>>

    @Query("""SELECT * FROM Watcher""")
    suspend fun getAllWatchers(): List<Watcher>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWatcher(value: Watcher)

    @Query("""DELETE FROM Watcher WHERE guid=:guid""")
    suspend fun removeWatcher(guid: String)
}