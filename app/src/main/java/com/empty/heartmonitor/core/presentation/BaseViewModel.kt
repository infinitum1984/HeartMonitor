package com.empty.heartmonitor.core.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            launch {
                Log.e("D_BaseViewModel", ": ${throwable.printStackTrace()}")
                _error.send(throwable.message ?: throwable::class.java.name)
            }
        }

    private val _error = Channel<String?>(Channel.BUFFERED)
    val error = _error.receiveAsFlow()

    protected val ioContext = Dispatchers.IO + exceptionHandler

    protected val uiContext = Dispatchers.Main + exceptionHandler

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun launch(context: CoroutineContext = uiContext, action: suspend () -> Unit) {
        viewModelScope.launch(context) {
            _isLoading.emit(true)
            action.invoke()
            _isLoading.emit(false)
        }
    }

    fun launchJob(context: CoroutineContext = uiContext, action: suspend () -> Unit): Job {
        return viewModelScope.launch(context) {
            _isLoading.emit(true)
            action.invoke()
            _isLoading.emit(false)
        }
    }
}