package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.toAnimalParameters
import com.example.petfinderremake.common.domain.model.toString
import com.example.petfinderremake.common.ext.getNavigationResult
import com.example.petfinderremake.common.ext.setNavigationResult
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.toString
import com.example.petfinderremake.features.filter.presentation.navigation.FILTER_KEY
import com.example.petfinderremake.features.filter.presentation.navigation.FilterNavigation
import com.example.petfinderremake.features.filter.presentation.navigation.RESULT_KEY
import com.example.petfinderremake.features.filter.presentation.screen.filter.FilterFragmentArgs
import com.example.petfinderremake.features.filter.presentation.screen.filter.FilterFragmentDirections
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetFinderFilterNavigation @Inject constructor(
    private val moshi: Moshi
) : FilterNavigation {

    override fun navigateToSelect(fragment: Fragment, selectNavArg: SelectNavArg) =
        with(fragment) {
            val arg = selectNavArg.toString(moshi)
            val action = FilterFragmentDirections.actionFilterFragmentToSelectFragment(arg)
            findNavController().navigate(action)
        }

    override fun getFilterNavArg(fragment: Fragment): AnimalParameters = with(fragment) {
        val args: FilterFragmentArgs by navArgs()
        return args.filterNavArg.toAnimalParameters(moshi)
    }

    override fun clearFilterNavArgs(fragment: Fragment) = with(fragment) {
        arguments?.remove(FILTER_KEY)
        Unit
    }

    override fun observeResultNavArg(fragment: Fragment): Flow<AnimalParameters> =
        with(fragment) {
            getNavigationResult(RESULT_KEY).asFlow().map { it.toAnimalParameters(moshi) }
        }

    override fun clearResultNavArg(fragment: Fragment) =
        with(fragment) {
            arguments?.remove(RESULT_KEY)
            Unit
        }

    override fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters) =
        with(fragment) {
            val result = resultNavArg.toString(moshi)
            setNavigationResult(result, RESULT_KEY)
            findNavController().popBackStack()
            Unit
        }
}