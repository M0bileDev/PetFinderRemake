package com.example.petfinderremake.features.notifications.presentation.screen.details

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetSingleNotificationUseCase
import com.example.petfinderremake.common.domain.usecase.notification.update.UpdateNotificationUseCase
import com.example.petfinderremake.common.ext.getValueOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class NotificationDetailsViewModel @Inject constructor(
    private val getSingleNotificationUseCase: GetSingleNotificationUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) : ViewModel() {

    private val notificationDetailsUiStateSubject =
        BehaviorSubject.createDefault(NotificationDetailsUiState())
    val notificationDetailsUiState = notificationDetailsUiStateSubject.hide()

    private val subscriptions = CompositeDisposable()

    fun setupNavArgs(notificationId: Long) {
        getSingleNotification(notificationId)
    }

    private fun getSingleNotification(notificationId: Long) {
        getSingleNotificationUseCase(notificationId).subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        val notification = it.success
                        val uiState = notificationDetailsUiStateSubject.getValueOrThrow()
                        notificationDetailsUiStateSubject.onNext(uiState.copy(notification = notification))
                        updateNotificationDisplayed(notification.copy(displayed = true))
                    }
                }
            }.addTo(subscriptions)
    }

    private fun updateNotificationDisplayed(notification: Notification) {
        updateNotificationUseCase(notification).subscribeOn(Schedulers.io()).subscribe()
            .addTo(subscriptions)
    }

    override fun onCleared() {
        subscriptions.dispose()
    }

}