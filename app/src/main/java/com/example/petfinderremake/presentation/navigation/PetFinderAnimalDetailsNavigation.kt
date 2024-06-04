package com.example.petfinderremake.presentation.navigation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petfinderremake.R
import com.example.petfinderremake.common.presentation.navigation.AnimalDetailsNavigation
import com.example.petfinderremake.common.presentation.navigation.model.ANIMAL_DETAILS_KEY
import javax.inject.Inject

class PetFinderAnimalDetailsNavigation @Inject constructor() : AnimalDetailsNavigation {

    override fun getNavArgs(fragment: Fragment): Long = with(fragment) {
        return arguments?.getLong(ANIMAL_DETAILS_KEY) ?: throw Exception()
    }

    override fun navigateToAnimalDetails(fragment: Fragment, id: Long) = with(fragment) {
        val bundle = bundleOf(ANIMAL_DETAILS_KEY to id)
        findNavController().navigate(R.id.animalDetailsFragment, bundle)
    }
}