package com.empty.heartmonitor.tracking.presentation

import com.empty.heartmonitor.core.presentation.BaseViewModel
import com.empty.heartmonitor.tracking.domain.TrackingRepository
import kotlinx.coroutines.Dispatchers

class TrackingViewModel(private val trackingRepository: TrackingRepository) : BaseViewModel() {

    val allWatchers = trackingRepository.watchers
    val trackingState = trackingRepository.trackingState
    val isMonitoring = trackingRepository.monitoringState
    fun addWatcher(guid: String, name: String) = launch(Dispatchers.IO) {
        trackingRepository.addWatcher(guid, name)
    }

    fun removeWatcher(guid: String) = launch(Dispatchers.IO) {
        trackingRepository.removeWatcher(guid)
    }

    fun onTrackingClick() = launch {
        trackingRepository.changeTrackingState()
    }

    fun changeMonitoring(isMonitoring: Boolean) = launch {
        trackingRepository.changeMonitoring(isMonitoring)
    }

}