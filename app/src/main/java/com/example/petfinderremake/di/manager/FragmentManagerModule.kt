package com.example.petfinderremake.di.manager

import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.common.presentation.manager.permission.PermissionManager
import com.example.petfinderremake.presentation.manager.PetFinderNotificationManager
import com.example.petfinderremake.presentation.manager.PetFinderPermissionManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(FragmentComponent::class)]
abstract class FragmentManagerModule {

    @[Binds FragmentScoped]
    abstract fun bindPermissionManager(permissionManager: PetFinderPermissionManager): PermissionManager

}

@[Module InstallIn(SingletonComponent::class)]
abstract class ApplicationManagerModule {

    @[Binds Singleton]
    abstract fun bindNotificationManager(notificationManager: PetFinderNotificationManager): NotificationManager
}

