package com.example.petfinderremake.common.presentation.manager.permission

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface PermissionSender {

    val permissionEvent: Channel<PermissionEvent>

    sealed interface PermissionEvent {
        data class ShowPermissionRationale(val permission: String) : PermissionEvent
    }

    fun getPermissionEvent(): Flow<PermissionEvent> = permissionEvent.receiveAsFlow()
}