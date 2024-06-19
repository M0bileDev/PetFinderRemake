package com.example.petfinderremake.presentation.activity

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.notification.get.GetNotDisplayedNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getNotDisplayedNotificationsUseCase: GetNotDisplayedNotificationsUseCase
) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val mainActivitySubject = BehaviorSubject.createDefault(MainActivityUiState())
    val mainActivityUiState = mainActivitySubject
        .hide()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    init {
        observeAllNotDisplayedNotifications()
    }

    private fun observeAllNotDisplayedNotifications() {
        getNotDisplayedNotificationsUseCase()
            .subscribe { result ->
                result.onSuccess {
                    mainActivitySubject.onNext(MainActivityUiState(it.success.isNotEmpty()))
                }
            }.addTo(subscriptions)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}