package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg

interface SelectNavigation {
    fun getSelectNavArg(fragment: Fragment): SelectNavArg
    fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters)
}