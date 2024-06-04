package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import kotlinx.coroutines.flow.Flow

interface FilterNavigation {
    fun navigateToSelect(fragment: Fragment, selectNavArg: SelectNavArg)
    fun getFilterNavArg(fragment: Fragment): AnimalParameters
    fun clearFilterNavArgs(fragment: Fragment)
    fun observeResultNavArg(fragment: Fragment): Flow<AnimalParameters>
    fun clearResultNavArg(fragment: Fragment)
    fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters)
}