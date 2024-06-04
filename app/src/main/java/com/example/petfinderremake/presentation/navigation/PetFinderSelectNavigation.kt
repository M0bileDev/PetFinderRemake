package com.example.petfinderremake.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.toString
import com.example.petfinderremake.common.ext.setNavigationResult
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.toSelectNavArg
import com.example.petfinderremake.features.filter.presentation.navigation.RESULT_KEY
import com.example.petfinderremake.features.filter.presentation.navigation.SelectNavigation
import com.example.petfinderremake.features.filter.presentation.screen.select.SelectFragmentArgs
import com.squareup.moshi.Moshi
import javax.inject.Inject

class PetFinderSelectNavigation @Inject constructor(
    private val moshi: Moshi
) : SelectNavigation {
    override fun getSelectNavArg(fragment: Fragment): SelectNavArg =
        with(fragment) {
            val args: SelectFragmentArgs by navArgs()
            val stringValue = args.selectNavArg
            return stringValue.toSelectNavArg(moshi)
        }

    override fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters) =
        with(fragment) {
            val result = resultNavArg.toString(moshi)
            setNavigationResult(result, RESULT_KEY)
            findNavController().popBackStack()
            Unit
        }
}