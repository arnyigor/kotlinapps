package com.arny.kotlinapps.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arny.kotlinapps.domain.LoadingDataUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val useCase = LoadingDataUseCase()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private val _result = MutableStateFlow<String?>(null)
    val result = _result.asStateFlow()

    private val _error = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        BufferOverflow.DROP_OLDEST
    )
    val error = _error.asSharedFlow()

    private val _info = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        BufferOverflow.DROP_OLDEST
    )
    val info = _info.asSharedFlow()

    init {
        loadData()
    }

    fun reload() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            loadUseCase()
        }
    }

    private suspend fun loadUseCase() {
        _result.emitAll(
            flow { emit(useCase()) }
                .onStart { setLoading(true) }
                .onCompletion { setLoading(false) }
                .onEach {
                    _result.value = it
                    if (_progress.value < 100) {
                        _progress.value += 10
                        loadUseCase()
                    }
                }
                .map {
                    "Result is $it"
                }
                .catch { cause: Throwable -> catchErrors(cause) }
        )
    }

    private fun catchErrors(throwable: Throwable) {
        when (throwable) {
            is IllegalStateException -> {
                _info.tryEmit(throwable.message.orEmpty())
            }
            is RuntimeException -> {
                _error.tryEmit(throwable.message.orEmpty())
            }
            else -> {
                _error.tryEmit(throwable.message.orEmpty())
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            _info.tryEmit("")
        }
        _loading.value = loading
    }
}