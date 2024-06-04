package com.example.petfinderremake.features.filter.di

import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.FilterNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.SelectNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.TestPetFinderBreedNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.TestPetFinderCommonNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.TestPetFinderFilterNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.TestPetFinderTypeNavigation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// For multi select tests
@[Module InstallIn(SingletonComponent::class)]
abstract class TestSelectModuleBreeds {

    @Binds
    abstract fun bindTestBreedSelectNavigation(selectNavigation: TestPetFinderBreedNavigation): SelectNavigation

}

// For single select tests
@[Module InstallIn(SingletonComponent::class)]
abstract class TestSelectModuleTypes {

    @Binds
    abstract fun bindTestTypeSelectNavigation(selectNavigation: TestPetFinderTypeNavigation): SelectNavigation
}


@[Module InstallIn(SingletonComponent::class)]
abstract class TestSelectModuleCommon {

    @Binds
    abstract fun bindTestAppNavigation(commonNavigation: TestPetFinderCommonNavigation): CommonNavigation

    @Binds
    abstract fun bindFilterNavigation(filterNavigation: TestPetFinderFilterNavigation): FilterNavigation
}