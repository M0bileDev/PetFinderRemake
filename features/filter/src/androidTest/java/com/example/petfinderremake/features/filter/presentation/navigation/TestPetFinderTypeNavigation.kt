package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import com.example.petfinderremake.features.filter.utils.TestUtils.testNames
import javax.inject.Inject

class TestPetFinderTypeNavigation @Inject constructor() : SelectNavigation {
    override fun getSelectNavArg(fragment: Fragment): SelectNavArg {
        return SelectNavArg(
            testNames,
            AnimalParameters.noAnimalParameters,
            SelectType.TYPE
        )
    }

    override fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters) {}

}