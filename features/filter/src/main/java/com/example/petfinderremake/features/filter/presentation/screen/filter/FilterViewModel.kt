package com.example.petfinderremake.features.filter.presentation.screen.filter

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.isNotEmpty
import com.example.petfinderremake.common.domain.result.error.Error
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.error.onNetworkError
import com.example.petfinderremake.common.domain.result.error.onStorageError
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.animal.delete.DeleteBreedsUseCase
import com.example.petfinderremake.common.domain.usecase.animal.get.GetAnimalBreedsUseCase
import com.example.petfinderremake.common.domain.usecase.animal.get.GetAnimalTypesUseCase
import com.example.petfinderremake.common.domain.usecase.animal.request.RequestAnimalBreedsUseCase
import com.example.petfinderremake.common.domain.usecase.animal.request.RequestAnimalTypesUseCase
import com.example.petfinderremake.common.ext.getValueOrThrow
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getAnimalTypesUseCase: GetAnimalTypesUseCase,
    private val requestAnimalTypesUseCase: RequestAnimalTypesUseCase,
    private val getAnimalBreedsUseCase: GetAnimalBreedsUseCase,
    private val requestAnimalBreedsUseCase: RequestAnimalBreedsUseCase,
    private val deleteBreedsUseCase: DeleteBreedsUseCase,
) : ViewModel() {

    sealed interface FilterEvent {
        data class NavigateToSelect(val selectNavArg: SelectNavArg) : FilterEvent
        data class NavigateBackWithResult(val resultNavArg: AnimalParameters) : FilterEvent
    }

    private val subscriptions = CompositeDisposable()

    private val networkErrorSubject = PublishSubject.create<NetworkError>()
    val networkError = networkErrorSubject.hide().map { it as Error }

    private val storageErrorSubject = PublishSubject.create<StorageError>()
    val storageError = storageErrorSubject.hide().map { it as Error }

    private val filterEventSubject = PublishSubject.create<FilterEvent>()
    val filterEvent = filterEventSubject.hide()

    private val animalTypesLoadingSubject = BehaviorSubject.createDefault(false)
    private val animalBreedsLoadingSubject = BehaviorSubject.createDefault(false)
    private val filterSubject = BehaviorSubject.createDefault(AnimalParameters.noAnimalParameters)
    private val animalTypesSubject = BehaviorSubject.createDefault<List<AnimalType>>(emptyList())
    private val animalBreedsSubject = BehaviorSubject.createDefault<List<AnimalBreeds>>(emptyList())

    val filterUiState = Observable.combineLatest(
        filterSubject,
        animalTypesSubject,
        animalBreedsSubject,
        animalTypesLoadingSubject,
        animalBreedsLoadingSubject
    ) { filter,
        typeState,
        breedState,
        animalTypesLoading,
        animalBreedsLoading ->

        FilterUiState(
            filter.isNotEmpty(),
            typeState,
            breedState,
            animalTypesLoading,
            animalBreedsLoading
        )
    }

    init {
        observeAnimalTypes()
        observeAnimalBreeds()
        requestAnimalTypes()
    }

    private fun observeAnimalTypes() {
        getAnimalTypesUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        animalTypesSubject.onNext(it.success)
                    }
                }
            }.addTo(subscriptions)
    }

    private fun observeAnimalBreeds() {
        getAnimalBreedsUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        animalBreedsSubject.onNext(it.success)
                    }
                }
            }.addTo(subscriptions)
    }

    private fun requestAnimalTypes() {
        requestAnimalTypesUseCase(
            onLoading = {
                animalTypesLoadingSubject.onNext(it)
            })
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                result
                    .onNetworkError(onNetworkError = {
                        networkErrorSubject.onNext(it)
                    })
                    .onStorageError(onStorageError = {
                        storageErrorSubject.onNext(it)
                    })
            }.addTo(subscriptions)
    }

    private fun requestAnimalBreeds(animalType: String) {
        requestAnimalBreedsUseCase(
            type = animalType,
            onLoading = {
                animalBreedsLoadingSubject.onNext(it)
            })
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                result.onNetworkError(onNetworkError = {
                    networkErrorSubject.onNext(it)
                })
                    .onStorageError(onStorageError = {
                        storageErrorSubject.onNext(it)
                    })
            }.addTo(subscriptions)
    }

    fun onApplyFilter() {
        val filter = filterSubject.getValueOrThrow()
        filterEventSubject.onNext(FilterEvent.NavigateBackWithResult(filter))
    }

    fun onClearFilter() {
        filterSubject.onNext(AnimalParameters.noAnimalParameters)
        deleteBreedsUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .addTo(subscriptions)
        requestAnimalTypes()
    }

    fun onRefresh() {
        val filter = filterSubject.getValueOrThrow()

        requestAnimalTypes()

        filter.type?.let { breed ->
            if (breed.isNotEmpty()) {
                requestAnimalBreeds(filter.type!!)
            }
        }
    }

    fun onSelectAnimalType() {
        navigateToSelect(SelectType.TYPE)
    }

    fun onSelectAnimalBreed() {
        navigateToSelect(SelectType.BREED)
    }

    private fun navigateToSelect(selectType: SelectType) {

        val types = animalTypesSubject.getValueOrThrow()
        val breeds = animalBreedsSubject.getValueOrThrow()

        val names = when (selectType) {
            SelectType.TYPE -> types.map { it.name }
            SelectType.BREED -> breeds.map { it.name }
        }

        val filter = filterSubject.getValueOrThrow()

        val selectNavArg = SelectNavArg(
            names = names,
            filter = filter,
            selectType = selectType,
        )
        filterEventSubject.onNext(FilterEvent.NavigateToSelect(selectNavArg))

    }

    fun setupFilterNavArg(arg: AnimalParameters) {
        updateFilter(arg)
    }

    fun setupResultNavArg(resultNavArg: AnimalParameters) {
        updateFilter(resultNavArg)
    }

    private fun updateFilter(filterArg: AnimalParameters) {

        val filter = filterSubject.getValueOrThrow()

        if (filterArg.type != filter.type) {
            filterArg.type?.let { type ->
                if (type.isNotEmpty()) {
                    requestAnimalBreeds(type)
                }
            } ?: deleteBreedsUseCase()
        }
        if (filter != filterArg) {
            filterSubject.onNext(filterArg)
        }

    }

    override fun onCleared() {
        subscriptions.dispose()
    }

}