package com.example.petfinderremake.features.notifications.presentation.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetAllNotificationsUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.get.GetNotificationPermanentlyDeniedUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.put.PutNotificationsPermanentlyDeniedUseCase
import com.example.petfinderremake.common.presentation.manager.permission.PermissionSender
import com.example.petfinderremake.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val putNotificationsPermanentlyDeniedUseCase: PutNotificationsPermanentlyDeniedUseCase,
    private val getNotificationPermanentlyDeniedUseCase: GetNotificationPermanentlyDeniedUseCase,
    private val getAllNotificationsUseCase: GetAllNotificationsUseCase,
) : ViewModel(), PermissionSender {
    override val permissionEvent: Channel<PermissionSender.PermissionEvent> = Channel()

    sealed interface NotificationEvent {
        data class NavigateToNotificationDetails(val notificationId: Long) : NotificationEvent
    }

    private var _notificationEvent = Channel<NotificationEvent>()
    val notificationEvent get() = _notificationEvent.receiveAsFlow()

    private val notifications = MutableStateFlow(emptyList<Notification>())

    //    Default true because most of the OS versions not require POST_NOTIFICATIONS permission
    private val notificationsGranted = MutableStateFlow(true)
    private val notificationsPermanentlyDenied = MutableStateFlow(false)

    private var runPermissionsRationaleJob: Job? = null
    private var navigateJob: Job? = null

    val notificationUiState = combine(
        notificationsGranted,
        notificationsPermanentlyDenied,
        notifications
    ) { notificationsGranted, notificationsPermanentlyDenied, notifications ->

        val notificationState = when {
            !notificationsGranted && notificationsPermanentlyDenied -> NotificationUiState(
                permanentlyDenied = true
            )

            !notificationsGranted -> NotificationUiState(
                denied = true
            )

            else -> NotificationUiState(
                granted = true
            )
        }

        Logger.d("log001 notificationState $notificationState")
        notificationState.copy(
            notifications = notifications
        )
    }

    init {
        observeNotificationsPermanentlyDenied()
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            getAllNotificationsUseCase().collectLatest { result ->
                with(result) {
                    onSuccess {
                        notifications.value = it.success
                    }
                }
            }
        }
    }

    private fun observeNotificationsPermanentlyDenied() {
        viewModelScope.launch {
            getNotificationPermanentlyDeniedUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            notificationsPermanentlyDenied.value = it.success
                        }
                    }
                }
        }
    }

    fun runPermissionRationale(permission: String) {
        runPermissionsRationaleJob?.cancel()
        runPermissionsRationaleJob = viewModelScope.launch {
            permissionEvent.send(PermissionSender.PermissionEvent.ShowPermissionRationale(permission))
        }
    }

    fun updateActionGranted(isGranted: Boolean) {
        notificationsGranted.value = isGranted
    }

    fun updateNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean) {
        viewModelScope.launch {
            putNotificationsPermanentlyDeniedUseCase(isPermanentlyDenied)
        }
    }

    fun navigateToNotificationDetails(notificationId: Long) {
        navigateJob?.cancel()
        navigateJob = viewModelScope.launch {
            _notificationEvent.send(NotificationEvent.NavigateToNotificationDetails(notificationId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        runPermissionsRationaleJob?.cancel()
        navigateJob?.cancel()
    }

}