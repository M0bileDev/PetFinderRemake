package com.example.petfinderremake.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetNotDisplayedNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getNotDisplayedNotificationsUseCase: GetNotDisplayedNotificationsUseCase
) : ViewModel() {

    private var _mainActivityUiState = MutableStateFlow(MainActivityUiState())
    val mainActivityUiState get() = _mainActivityUiState.asStateFlow()

    init {
        observeAllNotDisplayedNotifications()
    }

    private fun observeAllNotDisplayedNotifications() {
        viewModelScope.launch {
            getNotDisplayedNotificationsUseCase().collectLatest { result ->
                with(result) {
                    onSuccess {
                        _mainActivityUiState.value = MainActivityUiState(it.success.isNotEmpty())
                    }
                }
            }
        }
    }

}