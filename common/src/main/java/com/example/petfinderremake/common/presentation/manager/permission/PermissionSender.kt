package com.example.petfinderremake.common.presentation.manager.permission

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

interface PermissionSender {

    val permissionEventSubject: PublishSubject<PermissionEvent>

    sealed interface PermissionEvent {
        data class ShowPermissionRationale(val permission: String) : PermissionEvent
    }

    fun getPermissionEvent(): Observable<PermissionEvent> = permissionEventSubject.hide()
}