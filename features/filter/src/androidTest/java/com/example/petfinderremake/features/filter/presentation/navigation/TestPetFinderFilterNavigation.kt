package com.example.petfinderremake.features.filter.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TestPetFinderFilterNavigation @Inject constructor() : FilterNavigation {
    override fun navigateToSelect(fragment: Fragment, selectNavArg: SelectNavArg) {}

    override fun getFilterNavArg(fragment: Fragment): AnimalParameters {
        return AnimalParameters.noAnimalParameters
    }

    override fun clearFilterNavArgs(fragment: Fragment) {
    }

    override fun observeResultNavArg(fragment: Fragment): Observable<AnimalParameters> {
        return Observable.just(AnimalParameters.noAnimalParameters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun clearResultNavArg(fragment: Fragment) {
    }

    override fun navigateBackWithResult(fragment: Fragment, resultNavArg: AnimalParameters) {
    }

}