package com.example.petfinderremake.features.discover.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.error.onNetworkError
import com.example.petfinderremake.common.domain.result.error.onStorageError
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.animal.get.GetAnimalTypesUseCase
import com.example.petfinderremake.common.domain.usecase.animal.request.RequestAnimalTypesUseCase
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterEnum
import com.example.petfinderremake.common.presentation.adapter.model.createAnimalAdapterModel
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsSender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender.GalleryArg.Companion.runAction
import com.example.petfinderremake.features.discover.domain.usecase.get.GetDiscoverPaginatedAnimalsUseCase
import com.example.petfinderremake.features.discover.domain.usecase.request.RequestDiscoverPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val requestDiscoverPageUseCase: RequestDiscoverPageUseCase,
    private val getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase,
    private val requestAnimalTypesUseCase: RequestAnimalTypesUseCase,
    private val getAnimalTypesUseCase: GetAnimalTypesUseCase,
) : ViewModel(), GallerySender, AnimalDetailsSender {

    override val gallerySenderEvent: Channel<GallerySender.SenderEvent> = Channel()
    override val animalDetailsSenderEvent: Channel<AnimalDetailsSender.SenderEvent> = Channel()

    sealed interface DiscoverEvent {
        data class NavigateToSearch(val typeName: String) : DiscoverEvent
    }

    private var navigateToSearchJob: Job? = null
    private var navigateToAnimalDetailsJob: Job? = null
    private var gallerySenderJob: Job? = null

    fun navigateToSearch(typeName: String = "") {
        navigateToSearchJob?.cancel()
        navigateToSearchJob = viewModelScope.launch {
            discoverEventChannel.send(DiscoverEvent.NavigateToSearch(typeName))
        }
    }

    override fun navigateToAnimalDetails(id: Long) {
        navigateToAnimalDetailsJob?.cancel()
        navigateToAnimalDetailsJob = viewModelScope.runAnimalDetailsAction(id)
    }

    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionWithId = { actionId ->
            val animal = paginatedAnimals.value.animals.find { animal -> animal.id == actionId }
                ?: throw Exception()
            val media = animal.media
            gallerySenderJob?.cancel()
            gallerySenderJob = viewModelScope.runGalleryAction(media)
        })
    }

    private var discoverEventChannel = Channel<DiscoverEvent>()
    val discoverEvent = discoverEventChannel.receiveAsFlow()

    private val discoverPageLoading = MutableStateFlow(false)
    private val animalTypesLoading = MutableStateFlow(false)
    private val paginatedAnimals = MutableStateFlow(PaginatedAnimals.initPaginatedAnimals)
    private val animalTypes = MutableStateFlow<List<AnimalType>>(emptyList())

    private var requestDiscoverPageJob: Job? = null
    private var observeDiscoverPaginatedAnimalsJob: Job? = null
    private var requestAnimalTypesJob: Job? = null
    private var observeAnimalTypesJob: Job? = null

    private var networkErrorChannel = Channel<NetworkError>()
    val networkError get() = networkErrorChannel.receiveAsFlow()

    private var storageErrorChannel = Channel<StorageError>()
    val storageError get() = storageErrorChannel.receiveAsFlow()


    val discoverUiState = combine(
        discoverPageLoading,
        animalTypesLoading,
        paginatedAnimals,
        animalTypes
    ) { discoverPageLoading, animalTypesLoading, paginatedAnimals, animalTypes ->

        val (animals, pagination) = paginatedAnimals

        val totalCount =
            if (!discoverPageLoading) pagination.totalCount else Int.MIN_VALUE
        val animalsWithDetails = if (!discoverPageLoading) animals else emptyList()
        val types = if (!animalTypesLoading) animalTypes else emptyList()
        val adapterModels =
            if (animalsWithDetails.isEmpty()) {
                emptyList()
            } else {
                animalsWithDetails.createAnimalAdapterModel(AnimalAdapterEnum.ANIMAL_GRID_ITEM)
            }


        DiscoverUiState(
            isTopLoading = discoverPageLoading,
            isBottomLoading = animalTypesLoading,
            totalCount = totalCount,
            adapterModels = adapterModels,
            animalTypes = types,
        )
    }

    init {
        observeDiscoverPage()
        observeAnimalTypes()
//        requestData()
    }

    private fun observeDiscoverPage() {
        observeDiscoverPaginatedAnimalsJob?.cancel()
        observeDiscoverPaginatedAnimalsJob = viewModelScope.launch {
            getDiscoverPaginatedAnimalsUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            paginatedAnimals.value = it.success
                        }
                    }

                }
        }
    }

    private fun observeAnimalTypes() {
        observeAnimalTypesJob?.cancel()
        observeAnimalTypesJob = viewModelScope.launch {
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

    fun requestData() {
        requestDiscoverPage()
        requestAnimalTypes()
    }

    private fun requestDiscoverPage() {
        requestDiscoverPageJob?.cancel()
        requestDiscoverPageJob = viewModelScope.launch {
            requestDiscoverPageUseCase(
                onLoading = { loading ->
                    discoverPageLoading.value = loading
                })
                .onNetworkError(onNetworkError = {
                    networkErrorChannel.send(it)
                })
                .onStorageError(onStorageError = {
                    storageErrorChannel.send(it)
                })
        }
    }

    private fun requestAnimalTypes() {
        requestAnimalTypesJob?.cancel()
        requestAnimalTypesJob = viewModelScope.launch {
            requestAnimalTypesUseCase(
                onLoading = { loading ->
                    animalTypesLoading.value = loading
                })
                .onNetworkError(onNetworkError = {
                    networkErrorChannel.send(it)
                })
                .onStorageError(onStorageError = {
                    storageErrorChannel.send(it)
                })
        }
    }

    override fun onCleared() {
        requestDiscoverPageJob?.cancel()
        requestAnimalTypesJob?.cancel()
        observeDiscoverPaginatedAnimalsJob?.cancel()
        observeAnimalTypesJob?.cancel()
        gallerySenderJob?.cancel()
        navigateToSearchJob?.cancel()
        navigateToAnimalDetailsJob?.cancel()
    }

}

