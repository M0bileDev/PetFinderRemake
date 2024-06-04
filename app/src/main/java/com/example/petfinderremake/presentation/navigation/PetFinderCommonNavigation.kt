package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import javax.inject.Inject

class PetFinderCommonNavigation @Inject constructor() : CommonNavigation {

    override fun navigateBack(fragment: Fragment) = with(fragment) {
        findNavController().popBackStack()
    }
}