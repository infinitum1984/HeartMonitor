package com.empty.heartmonitor.profile

import com.empty.heartmonitor.core.ext.fetchToken
import com.empty.heartmonitor.core.presentation.BaseViewModel
import com.empty.heartmonitor.store.AppDataStore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val appDataStore: AppDataStore) : BaseViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    init {
        launch(Dispatchers.IO) {
            _name.emit(appDataStore.getUserName())
            _token.emit(
                FirebaseMessaging.getInstance().fetchToken()
            )
        }
    }

    fun saveName(name: String) = launch(Dispatchers.IO) {
        appDataStore.setUserName(name)
    }

}