package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.utils.TestUtils
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import javax.inject.Inject

class TestPetFinderBreedNavigation @Inject constructor() : SelectNavigation {
    override fun getSelectNavArg(fragment: Fragment): SelectNavArg {
        return SelectNavArg(
            TestUtils.testNames,
            AnimalParameters.noAnimalParameters,
            SelectType.BREED
        )
    }

    override fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters){}

}