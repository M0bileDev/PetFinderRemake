package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import javax.inject.Inject

class TestPetFinderCommonNavigation @Inject constructor() : CommonNavigation{
    override fun navigateBack(fragment: Fragment): Boolean {
        return true
    }

}