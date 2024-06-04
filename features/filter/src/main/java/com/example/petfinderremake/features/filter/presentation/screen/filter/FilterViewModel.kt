package com.example.petfinderremake.features.filter.presentation.screen.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.isNotEmpty
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
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private var networkErrorChannel = Channel<NetworkError>()
    val networkError get() = networkErrorChannel.receiveAsFlow()

    private var storageErrorChannel = Channel<StorageError>()
    val storageError get() = storageErrorChannel.receiveAsFlow()

    private var _filterEvent = Channel<FilterEvent>()
    val filterEvent get() = _filterEvent.receiveAsFlow()

    private val animalTypesLoading = MutableStateFlow(false)
    private val animalBreedsLoading = MutableStateFlow(false)
    private val filter = MutableStateFlow(AnimalParameters.noAnimalParameters)
    private val animalTypes = MutableStateFlow<List<AnimalType>>(emptyList())
    private val animalBreeds = MutableStateFlow<List<AnimalBreeds>>(emptyList())

    private var requestAnimalTypesJob: Job? = null
    private var requestAnimalBreedsJob: Job? = null
    private var navigateToSelectJob: Job? = null
    private var applyFilterJob: Job? = null
    private var clearFilterJob: Job? = null

    val filterUiState = combine(
        filter,
        animalTypes,
        animalBreeds,
        animalTypesLoading,
        animalBreedsLoading
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
        viewModelScope.launch {
            getAnimalTypesUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            animalTypes.value = it.success
                        }
                    }
                }
        }
    }

    private fun observeAnimalBreeds() {
        viewModelScope.launch {
            getAnimalBreedsUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            animalBreeds.value = it.success
                        }
                    }
                }
        }
    }

    private fun requestAnimalTypes() {
        requestAnimalTypesJob?.cancel()
        requestAnimalTypesJob = viewModelScope.launch {
            requestAnimalTypesUseCase(
                onLoading = {
                    animalTypesLoading.value = it
                })
                // TODO: handle errors
                .onNetworkError(onNetworkError = {
                    networkErrorChannel.send(it)
                })
                // TODO: handle errors
                .onStorageError(onStorageError = {
                    storageErrorChannel.send(it)
                })
        }
    }

    private fun requestAnimalBreeds(animalType: String) {
        requestAnimalBreedsJob?.cancel()
        requestAnimalBreedsJob = viewModelScope.launch {
            requestAnimalBreedsUseCase(
                type = animalType,
                onLoading = {
                    animalBreedsLoading.value = it
                })
                .onNetworkError(onNetworkError = {
                    networkErrorChannel.send(it)
                })
                .onStorageError(onStorageError = {
                    storageErrorChannel.send(it)
                })
        }
    }

    fun onApplyFilter() {
        applyFilterJob?.cancel()
        applyFilterJob = viewModelScope.launch {
            val filter = filter.value
            _filterEvent.send(FilterEvent.NavigateBackWithResult(filter))
        }
    }

    fun onClearFilter() = with(AnimalParameters.noAnimalParameters) {
        clearFilterJob?.cancel()
        clearFilterJob = viewModelScope.launch {
            filter.update { previousState ->
                previousState.copy(
                    type,
                    breed,
                    size,
                    gender,
                    age,
                    color,
                    coat,
                    status,
                    name,
                    organization,
                    goodWithChildren,
                    goodWithDogs,
                    goodWithCats,
                    houseTrained
                )
            }
            launch {
                deleteBreedsUseCase()
            }
            launch {
                requestAnimalTypes()
            }
        }
    }

    fun onRefresh() {
        val filter = filter.value

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
        navigateToSelectJob?.cancel()
        navigateToSelectJob = viewModelScope.launch {

            val names = when (selectType) {
                SelectType.TYPE -> animalTypes.value.map { it.name }
                SelectType.BREED -> animalBreeds.value.map { it.name }
            }

            val filter = filter.value

            val selectNavArg = SelectNavArg(
                names = names,
                filter = filter,
                selectType = selectType,
            )
            _filterEvent.send(FilterEvent.NavigateToSelect(selectNavArg))
        }
    }

    fun setupFilterNavArg(arg: AnimalParameters) {
        updateFilter(arg)
    }

    fun setupResultNavArg(resultNavArg: AnimalParameters) {
        updateFilter(resultNavArg)
    }

    private fun updateFilter(filterArg: AnimalParameters) {
        viewModelScope.launch {
            if (filterArg.type != filter.value.type) {
                filterArg.type?.let { type ->
                    if (type.isNotEmpty()) {
                        requestAnimalBreeds(type)
                    }
                } ?: deleteBreedsUseCase()
            }
            if (filter.value != filterArg) {
                filter.value = filterArg
            }
        }
    }

    override fun onCleared() {
        navigateToSelectJob?.cancel()
        clearFilterJob?.cancel()
        requestAnimalTypesJob?.cancel()
        requestAnimalBreedsJob?.cancel()
    }

}