package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import io.reactivex.rxjava3.core.Observable

interface FilterNavigation {
    fun navigateToSelect(fragment: Fragment, selectNavArg: SelectNavArg)
    fun getFilterNavArg(fragment: Fragment): AnimalParameters
    fun clearFilterNavArgs(fragment: Fragment)
    fun observeResultNavArg(fragment: Fragment): Observable<AnimalParameters>
    fun clearResultNavArg(fragment: Fragment)
    fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters)
}