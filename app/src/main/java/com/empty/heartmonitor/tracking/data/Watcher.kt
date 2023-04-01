package com.empty.heartmonitor.tracking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Watcher(
    @PrimaryKey
    val guid: String,
    val name: String
)