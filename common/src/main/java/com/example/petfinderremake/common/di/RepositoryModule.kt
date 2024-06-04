package com.example.petfinderremake.common.di

import com.example.petfinderremake.common.data.repositories.PetFinderAnimalRepository
import com.example.petfinderremake.common.data.repositories.PetFinderNotificationRepository
//import com.example.petfinderremake.common.data.repositories.PetFinderFilterRepository
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.repositories.NotificationRepository
//import com.example.petfinderremake.common.domain.repositories.FilterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class RepositoryModule {

    @[Binds Singleton]
    abstract fun bindAnimalRepository(
        petFinderAnimalRepository: PetFinderAnimalRepository
    ): AnimalRepository

    @[Binds Singleton]
    abstract fun bindNotificationRepository(
        petFinderNotificationRepository: PetFinderNotificationRepository
    ): NotificationRepository

}