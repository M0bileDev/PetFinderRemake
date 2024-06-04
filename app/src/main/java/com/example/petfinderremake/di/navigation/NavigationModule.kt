package com.example.petfinderremake.di.navigation

import com.example.petfinderremake.common.presentation.navigation.AnimalDetailsNavigation
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.features.discover.presentation.navigation.DiscoverNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.FilterNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.SelectNavigation
import com.example.petfinderremake.features.notifications.presentation.navigation.NotificationNavigation
import com.example.petfinderremake.features.search.presentation.navigation.SearchNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderAnimalDetailsNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderCommonNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderDiscoverNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderFilterNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderGalleryNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderNotificationNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderSearchNavigation
import com.example.petfinderremake.presentation.navigation.PetFinderSelectNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
abstract class NavigationModule {

    @[Binds Singleton]
    abstract fun bindDiscoverNavigation(discoverNavigation: PetFinderDiscoverNavigation): DiscoverNavigation

    @[Binds Singleton]
    abstract fun bindSearchNavigation(searchNavigation: PetFinderSearchNavigation): SearchNavigation

    @[Binds Singleton]
    abstract fun bindAnimalDetailsNavigation(animalDetailsNavigation: PetFinderAnimalDetailsNavigation): AnimalDetailsNavigation

    @[Binds Singleton]
    abstract fun bindAppNavigation(commonNavigation: PetFinderCommonNavigation): CommonNavigation

    @[Binds Singleton]
    abstract fun bindFilterNavigation(filterNavigation: PetFinderFilterNavigation): FilterNavigation

    @[Binds Singleton]
    abstract fun bindSelectNavigation(selectNavigation: PetFinderSelectNavigation): SelectNavigation

    @[Binds Singleton]
    abstract fun bindGalleryNavigation(galleryNavigation: PetFinderGalleryNavigation): GalleryNavigation

    @[Binds Singleton]
    abstract fun bindNotificationNavigation(notificationNavigation: PetFinderNotificationNavigation): NotificationNavigation
}