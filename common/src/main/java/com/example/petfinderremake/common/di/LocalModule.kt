package com.example.petfinderremake.common.di

import com.example.petfinderremake.common.data.local.cache.PetFinderAnimalCache
import com.example.petfinderremake.common.domain.local.AnimalStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class LocalModule {

    @[Binds Singleton]
    abstract fun bindAnimalCache(petFinderAnimalCache: PetFinderAnimalCache): AnimalStorage

}