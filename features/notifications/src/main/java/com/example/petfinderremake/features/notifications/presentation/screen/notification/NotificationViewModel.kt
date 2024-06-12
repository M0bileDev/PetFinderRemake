package com.example.petfinderremake.features.notifications.presentation.screen.notification

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetAllNotificationsUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.get.GetNotificationPermanentlyDeniedUseCase
import com.example.petfinderremake.common.domain.usecase.preferences.put.PutNotificationsPermanentlyDeniedUseCase
import com.example.petfinderremake.common.presentation.manager.permission.PermissionSender
import com.example.petfinderremake.logging.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val putNotificationsPermanentlyDeniedUseCase: PutNotificationsPermanentlyDeniedUseCase,
    private val getNotificationPermanentlyDeniedUseCase: GetNotificationPermanentlyDeniedUseCase,
    private val getAllNotificationsUseCase: GetAllNotificationsUseCase,
) : ViewModel(), PermissionSender {
    override val permissionEventSubject = PublishSubject.create<PermissionSender.PermissionEvent>()

    sealed interface NotificationEvent {
        data class NavigateToNotificationDetails(val notificationId: Long) : NotificationEvent
    }

    private val notificationEventSubject = PublishSubject.create<NotificationEvent>()
    val notificationEvent = notificationEventSubject.hide()

    private val notificationsSubject = BehaviorSubject.createDefault(emptyList<Notification>())
    //    Default true because most of the OS versions not require POST_NOTIFICATIONS permission
    private val notificationsGrantedSubject = BehaviorSubject.createDefault(true)
    private val notificationsPermanentlyDeniedSubject = BehaviorSubject.createDefault(false)

    private val subscriptions = CompositeDisposable()

    val notificationUiState = Observable.combineLatest(
        notificationsGrantedSubject,
        notificationsPermanentlyDeniedSubject,
        notificationsSubject
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

        notificationState.copy(
            notifications = notifications
        )
    }

    init {
        observeNotificationsPermanentlyDenied()
        observeNotifications()
    }

    private fun observeNotifications() {
        getAllNotificationsUseCase().subscribeOn(Schedulers.io()).subscribe { result ->
            with(result) {
                onSuccess {
                    notificationsSubject.onNext(it.success)
                }
            }
        }.addTo(subscriptions)
    }

    private fun observeNotificationsPermanentlyDenied() {
        getNotificationPermanentlyDeniedUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        notificationsPermanentlyDeniedSubject.onNext(it.success)
                    }
                }
            }.addTo(subscriptions)
    }

    fun runPermissionRationale(permission: String) {
        permissionEventSubject.onNext(
            PermissionSender.PermissionEvent.ShowPermissionRationale(
                permission
            )
        )
    }

    fun updateActionGranted(isGranted: Boolean) {
        notificationsGrantedSubject.onNext(isGranted)
    }

    fun updateNotificationsPermanentlyDenied(isPermanentlyDenied: Boolean) {
        putNotificationsPermanentlyDeniedUseCase(isPermanentlyDenied).subscribeOn(Schedulers.io())
            .subscribe().addTo(subscriptions)
    }

    fun navigateToNotificationDetails(notificationId: Long) {
        notificationEventSubject.onNext(
            NotificationEvent.NavigateToNotificationDetails(
                notificationId
            )
        )
    }

    override fun onCleared() {
        subscriptions.dispose()
    }

}