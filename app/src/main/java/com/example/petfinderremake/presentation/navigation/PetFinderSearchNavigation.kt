package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.toAnimalParameters
import com.example.petfinderremake.common.domain.model.toString
import com.example.petfinderremake.common.ext.getNavigationResult
import com.example.petfinderremake.features.filter.presentation.navigation.FILTER_KEY
import com.example.petfinderremake.features.filter.presentation.navigation.RESULT_KEY
import com.example.petfinderremake.features.search.presentation.navigation.SearchNavigation
import com.example.petfinderremake.features.search.presentation.screen.SearchFragmentArgs
import com.example.petfinderremake.features.search.presentation.screen.SearchFragmentDirections
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetFinderSearchNavigation @Inject constructor(
    private val moshi: Moshi
) : SearchNavigation {

    override fun navigateToFilter(fragment: Fragment, filterNavArg: AnimalParameters) =
        with(fragment) {
            val arg = filterNavArg.toString(moshi)
            val action =
                SearchFragmentDirections.actionSearchFragmentToFilterFragment(filterNavArg = arg)
            findNavController().navigate(action)
        }

    override fun getFilterNavArg(fragment: Fragment): AnimalParameters = with(fragment) {
        val args: SearchFragmentArgs by navArgs()
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
}