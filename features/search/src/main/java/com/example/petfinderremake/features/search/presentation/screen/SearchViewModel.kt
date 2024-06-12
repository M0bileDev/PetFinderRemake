package com.example.petfinderremake.features.search.presentation.screen

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.isEmpty
import com.example.petfinderremake.common.domain.model.isNotEmpty
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.error.Error
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import com.example.petfinderremake.common.domain.result.error.onNetworkError
import com.example.petfinderremake.common.domain.result.error.onStorageError
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.domain.usecase.animal.delete.DeleteAnimalsUseCase
import com.example.petfinderremake.common.domain.usecase.animal.delete.DeletePaginationUseCase
import com.example.petfinderremake.common.domain.usecase.animal.get.GetPaginationUseCase
import com.example.petfinderremake.common.ext.getValueOrThrow
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterEnum
import com.example.petfinderremake.common.presentation.adapter.model.addLoadMore
import com.example.petfinderremake.common.presentation.adapter.model.createAnimalAdapterModel
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsSender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender.GalleryArg.Companion.runAction
import com.example.petfinderremake.features.search.domain.usecase.get.GetAnimalsUseCase
import com.example.petfinderremake.features.search.domain.usecase.request.RequestAnimalsPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val requestAnimalsPageUseCase: RequestAnimalsPageUseCase,
    private val deleteAnimalsUseCase: DeleteAnimalsUseCase,
    private val deletePaginationUseCase: DeletePaginationUseCase,
    private val getAnimalsUseCase: GetAnimalsUseCase,
    private val getPaginationUseCase: GetPaginationUseCase,
) : ViewModel(), GallerySender, AnimalDetailsSender {

    sealed interface SearchEvent {
        data class NavigateToFilter(val filterNavArg: AnimalParameters) : SearchEvent
    }

    override val gallerySenderSubject = PublishSubject.create<GallerySender.SenderEvent>()
    override val animalDetailsSenderSubject =
        PublishSubject.create<AnimalDetailsSender.SenderEvent>()

    private val networkErrorSubject = PublishSubject.create<NetworkError>()
    val networkError = networkErrorSubject.hide().map { it as Error }

    private val storageErrorSubject = PublishSubject.create<StorageError>()
    val storageError = storageErrorSubject.map { it as Error }

    private val searchEventSubject = PublishSubject.create<SearchEvent>()
    val searchEvent = searchEventSubject.hide()

    private val filterSubject = BehaviorSubject.createDefault(AnimalParameters.noAnimalParameters)
    private val animalsSubject = BehaviorSubject.createDefault<List<AnimalWithDetails>>(emptyList())
    private val paginationSubject = BehaviorSubject.createDefault(Pagination.initPagination)
    private val loadingMoreSubject = BehaviorSubject.createDefault(false)
    private val loadingInitPageSubject = BehaviorSubject.createDefault(false)

    private val adapterModels =
        Observable.combineLatest(animalsSubject, paginationSubject) { animals, pagination ->

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

    val searchUiState = Observable.combineLatest(
        adapterModels,
        loadingMoreSubject,
        loadingInitPageSubject,
        filterSubject
    ) { adapterModels, loadingMore, loadingInitPage, filter ->

        SearchUiState(
            adapterModels = adapterModels,
            isLoadInitPage = loadingInitPage,
            isLoadMore = loadingMore,
            isFilterActive = filter.isNotEmpty()
        )
    }

    private val subscriptions = CompositeDisposable()

    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionWithId = { actionId ->
            val animals = animalsSubject.getValueOrThrow()
            val foundAnimal = animals.find { animal -> animal.id == actionId } ?: throw Exception()
            val media = foundAnimal.media
            runGalleryAction(media)
        })
    }

    override fun navigateToAnimalDetails(id: Long) {
        runAnimalDetailsAction(id)
    }

    init {
        observeAnimals()
        observePagination()
    }

    private fun observeAnimals() {
        getAnimalsUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                result.onSuccess {
                    animalsSubject.onNext(it.success)
                }
            }.addTo(subscriptions)
    }

    private fun observePagination() {
        getPaginationUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        paginationSubject.onNext(it.success)
                    }
                }
            }.addTo(subscriptions)
    }


    private fun requestAnimalsInitPage(animalParameters: AnimalParameters) {
        deleteAnimalsUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .addTo(subscriptions)
        deletePaginationUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe()
            .addTo(subscriptions)
        requestAnimalsPage(
            animalParameters = animalParameters,
            onLoading = {
                loadingInitPageSubject.onNext(it)
            })
    }


    fun onLoadMore() {
        requestAnimalsPage(
            pageToLoad = paginationSubject.getValueOrThrow().currentPage.inc(),
            animalParameters = filterSubject.getValueOrThrow(),
            onLoading = {
                loadingMoreSubject.onNext(it)
            })
    }

    private fun requestAnimalsPage(
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
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                result.onNetworkError { networkError ->
                    networkErrorSubject.onNext(networkError)
                }
                result.onStorageError { storageError ->
                    storageErrorSubject.onNext(storageError)
                }
            }
            .addTo(subscriptions)
    }

    fun onNavigateToFilter() {
        val filterNavArg = filterSubject.getValueOrThrow()
        searchEventSubject.onNext(SearchEvent.NavigateToFilter(filterNavArg))

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

        val filter = filterSubject.getValueOrThrow()

        if (filterArg == filter) return

        filterSubject.onNext(filterArg)

        if (filterArg.isEmpty()) {
            deleteAnimalsUseCase()
                .subscribeOn(Schedulers.io())
                .subscribe()
                .addTo(subscriptions)
        } else {
            requestAnimalsInitPage(filterArg)
        }


    }

    fun onRefresh() {
        val filter = filterSubject.getValueOrThrow()
        requestAnimalsInitPage(filter)
    }

    override fun onCleared() {
        subscriptions.dispose()
    }
}




