package com.example.petfinderremake.features.search.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import kotlinx.coroutines.flow.Flow

interface SearchNavigation {
    fun navigateToFilter(fragment: Fragment, filterNavArg: AnimalParameters)
    fun getFilterNavArg(fragment: Fragment): AnimalParameters
    fun clearFilterNavArgs(fragment: Fragment)
    fun observeResultNavArg(fragment: Fragment): Flow<AnimalParameters>
    fun clearResultNavArg(fragment: Fragment)
}