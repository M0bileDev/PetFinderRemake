package com.example.petfinderremake.features.search.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.isEmpty
import com.example.petfinderremake.common.domain.model.isNotEmpty
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.error.onNetworkError
import com.example.petfinderremake.common.domain.result.error.onStorageError
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.animal.delete.DeleteAnimalsUseCase
import com.example.petfinderremake.common.domain.usecase.animal.delete.DeletePaginationUseCase
import com.example.petfinderremake.common.domain.usecase.animal.get.GetPaginationUseCase
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterEnum
import com.example.petfinderremake.common.presentation.adapter.model.addLoadMore
import com.example.petfinderremake.common.presentation.adapter.model.createAnimalAdapterModel
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsSender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender.GalleryArg.Companion.runAction
import com.example.petfinderremake.features.search.domain.usecase.get.GetAnimalsUseCase
import com.example.petfinderremake.features.search.domain.usecase.request.RequestAnimalsPageUseCase
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
class SearchViewModel @Inject constructor(
    private val requestAnimalsPageUseCase: RequestAnimalsPageUseCase,
    private val deleteAnimalsUseCase: DeleteAnimalsUseCase,
    private val deletePaginationUseCase: DeletePaginationUseCase,
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val getPaginationUseCase: GetPaginationUseCase,
) : ViewModel(), GallerySender, AnimalDetailsSender {

    override val gallerySenderEvent: Channel<GallerySender.SenderEvent> = Channel()
    override val animalDetailsSenderEvent: Channel<AnimalDetailsSender.SenderEvent> = Channel()

    private var gallerySenderJob: Job? = null
    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionWithId = { actionId ->
            val animal = animals.value.find { animal -> animal.id == actionId } ?: throw Exception()
            val media = animal.media
            gallerySenderJob?.cancel()
            gallerySenderJob = viewModelScope.runGalleryAction(media)
        })
    }

    private var navigateToAnimalDetailsJob: Job? = null
    override fun navigateToAnimalDetails(id: Long) {
        navigateToAnimalDetailsJob?.cancel()
        navigateToAnimalDetailsJob = viewModelScope.runAnimalDetailsAction(id)
    }

    sealed interface SearchEvent {
        data class NavigateToFilter(val filterNavArg: AnimalParameters) : SearchEvent
    }

    private val filter = MutableStateFlow(AnimalParameters.noAnimalParameters)
    private val animals = MutableStateFlow<List<AnimalWithDetails>>(emptyList())
    private val pagination = MutableStateFlow(Pagination.initPagination)
    private val loadingMore = MutableStateFlow(false)
    private val loadingInitPage = MutableStateFlow(false)

    private var networkErrorChannel = Channel<NetworkError>()
    val networkError get() = networkErrorChannel.receiveAsFlow()

    private var storageErrorChannel = Channel<StorageError>()
    val storageError get() = storageErrorChannel.receiveAsFlow()

    private var _searchEvent = Channel<SearchEvent>()
    val searchEvent get() = _searchEvent.receiveAsFlow()

    private var requestAnimalsInitPageJob: Job? = null
    private var requestAnimalsNextPageJob: Job? = null
    private var navigateJob: Job? = null

    private val adapterModels =
        combine(animals, pagination) { animals, pagination ->

            val animalsConfirmed = animals.isNotEmpty()
            val paginationConfirmed = pagination != Pagination.initPagination
            val dataCompleted = (animalsConfirmed && paginationConfirmed)

            val adapterModels =
                if (dataCompleted) {
                    val adapterList =
                        animals.createAnimalAdapterModel(AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM)
                    if (pagination.canLoadMore) {
                        adapterList.addLoadMore(pagination.totalCount)
                    } else {
                        adapterList
                    }
                } else {
                    emptyList()
                }

            adapterModels
        }

    val searchUiState = combine(
        adapterModels,
        loadingMore,
        loadingInitPage,
        filter
    ) { adapterModels, loadingMore, loadingInitPage, filter ->

        SearchUiState(
            adapterModels = adapterModels,
            isLoadInitPage = loadingInitPage,
            isLoadMore = loadingMore,
            isFilterActive = filter.isNotEmpty()
        )
    }

    init {
        observeAnimals()
        observePagination()
    }

    private fun observeAnimals() {
        viewModelScope.launch {
            getAnimalsUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            animals.value = it.success
                        }
                    }
                }
        }
    }

    private fun observePagination() {
        viewModelScope.launch {
            getPaginationUseCase()
                .collectLatest { result ->
                    with(result) {
                        onSuccess {
                            pagination.value = it.success
                        }
                    }
                }

        }
    }


    private fun requestAnimalsInitPage(animalParameters: AnimalParameters) {
        requestAnimalsInitPageJob?.cancel()
        requestAnimalsInitPageJob = viewModelScope.launch {
            launch { deleteAnimalsUseCase() }
            launch { deletePaginationUseCase() }
            launch {
                requestAnimalsPage(
                    animalParameters = animalParameters,
                    onLoading = {
                        loadingInitPage.value = it
                    })
            }
        }
    }


    fun onLoadMore() {
        requestAnimalsNextPageJob?.cancel()
        requestAnimalsNextPageJob = viewModelScope.launch {
            requestAnimalsPage(
                pageToLoad = pagination.value.currentPage.inc(),
                animalParameters = filter.value,
                onLoading = {
                    loadingMore.value = it
                })
        }
    }

    private suspend fun requestAnimalsPage(
        pageToLoad: Int = 1,
        animalParameters: AnimalParameters,
        onLoading: (Boolean) -> Unit,
    ) {
        requestAnimalsPageUseCase(
            animalParameters = animalParameters,
            pageToLoad = pageToLoad,
            onLoading = { isLoading ->
                onLoading(isLoading)
            })
            .onNetworkError(onNetworkError = {
                networkErrorChannel.send(it)
            })
            .onStorageError(onStorageError = {
                storageErrorChannel.send(it)
            })
    }

    fun onNavigateToFilter() {
        navigateJob?.cancel()
        navigateJob = viewModelScope.launch {
            val filterNavArg = filter.value
            _searchEvent.send(SearchEvent.NavigateToFilter(filterNavArg))
        }
    }

    fun setupFilterNavArg(arg: AnimalParameters) {
        if (arg.isNotEmpty()) {
            updateFilter(arg)
        }
    }

    fun setupResultNavArg(resultNavArg: AnimalParameters) {
        updateFilter(resultNavArg)
    }

    private fun updateFilter(filterArg: AnimalParameters) {
        viewModelScope.launch {
            if (filterArg == filter.value) return@launch

            filter.value = filterArg

            if (filterArg.isEmpty()) {
                deleteAnimalsUseCase()
            } else {
                requestAnimalsInitPage(filterArg)
            }

        }
    }

    fun onRefresh() {
        val filter = filter.value
        requestAnimalsInitPage(filter)
    }

    override fun onCleared() {
        navigateJob?.cancel()
        requestAnimalsInitPageJob?.cancel()
        requestAnimalsNextPageJob?.cancel()
        gallerySenderJob?.cancel()
        navigateToAnimalDetailsJob?.cancel()
    }
}




