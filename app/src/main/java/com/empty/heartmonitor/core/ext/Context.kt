package com.empty.heartmonitor.core.ext

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it -> it.service.className == service.name }
}