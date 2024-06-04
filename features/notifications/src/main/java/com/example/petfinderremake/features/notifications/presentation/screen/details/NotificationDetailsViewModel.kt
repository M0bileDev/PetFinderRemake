package com.example.petfinderremake.features.notifications.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetSingleNotificationUseCase
import com.example.petfinderremake.common.domain.usecase.notification.update.UpdateNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationDetailsViewModel @Inject constructor(
    private val getSingleNotificationUseCase: GetSingleNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) : ViewModel() {

    private var _notificationDetailsUiState = MutableStateFlow(NotificationDetailsUiState())
    val notificationDetailsUiState get() = _notificationDetailsUiState.asStateFlow()

    private var getSingleNotificationJob: Job? = null
    private var updateNotificationDisplayedJob: Job? = null

    fun setupNavArgs(notificationId: Long) {
        getSingleNotification(notificationId)
    }

    private fun getSingleNotification(notificationId: Long) {
        getSingleNotificationJob?.cancel()
        getSingleNotificationJob = viewModelScope.launch {
            getSingleNotificationUseCase(notificationId).collectLatest { result ->
                with(result) {
                    onSuccess {
                        val notification = it.success
                        _notificationDetailsUiState.value = NotificationDetailsUiState(notification)
                        updateNotificationDisplayed(notification.copy(displayed = true))
                    }
                }
            }
        }
    }

    private fun updateNotificationDisplayed(notification: Notification) {
        updateNotificationDisplayedJob?.cancel()
        updateNotificationDisplayedJob = viewModelScope.launch {
            updateNotificationUseCase(notification)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getSingleNotificationJob?.cancel()
        updateNotificationDisplayedJob?.cancel()
    }

}