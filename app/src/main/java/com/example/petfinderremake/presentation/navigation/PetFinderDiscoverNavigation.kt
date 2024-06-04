package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.toString
import com.example.petfinderremake.features.discover.presentation.navigation.DiscoverNavigation
import com.example.petfinderremake.features.discover.presentation.screen.DiscoverFragmentDirections
import com.squareup.moshi.Moshi
import javax.inject.Inject

class PetFinderDiscoverNavigation @Inject constructor(
    private val moshi: Moshi
) : DiscoverNavigation, DefaultNavOptions {

    private val navOptions = getBottomNavOptions()

    override fun navigateToSearch(fragment: Fragment, typeName: String) = with(fragment) {
        val animalParameters =
            if (typeName.isEmpty()) AnimalParameters.noAnimalParameters else AnimalParameters(type = typeName)
        val arg = animalParameters.toString(moshi)
        val action =
            DiscoverFragmentDirections.actionDiscoverFragmentToSearchFragment(filterNavArg = arg)
        findNavController().navigate(action, navOptions)
    }
}