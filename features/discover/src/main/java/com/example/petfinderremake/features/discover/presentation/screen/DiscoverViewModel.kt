package com.example.petfinderremake.features.discover.presentation.screen

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.result.error.Error
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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val requestDiscoverPageUseCase: RequestDiscoverPageUseCase,
    private val getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase,
    private val requestAnimalTypesUseCase: RequestAnimalTypesUseCase,
    private val getAnimalTypesUseCase: GetAnimalTypesUseCase,
) : ViewModel(), GallerySender, AnimalDetailsSender {

    sealed interface DiscoverEvent {
        data class NavigateToSearch(val typeName: String) : DiscoverEvent
    }

    private val subscriptions = CompositeDisposable()

    override val gallerySenderSubject = PublishSubject.create<GallerySender.SenderEvent>()
    override val animalDetailsSenderSubject =
        PublishSubject.create<AnimalDetailsSender.SenderEvent>()

    private val discoverPageLoadingSubject = BehaviorSubject.createDefault(false)
    private val animalTypesLoadingSubject = BehaviorSubject.createDefault(false)
    private val paginatedAnimalsSubject =
        BehaviorSubject.createDefault(PaginatedAnimals.initPaginatedAnimals)
    private val animalTypesSubject = BehaviorSubject.createDefault<List<AnimalType>>(emptyList())

    private val discoverEventSubject = PublishSubject.create<DiscoverEvent>()
    val discoverEvent = discoverEventSubject.hide()

    private val networkErrorSubject = PublishSubject.create<NetworkError>()
    val networkError = networkErrorSubject.hide().map { it as Error }

    private var storageErrorSubject = PublishSubject.create<StorageError>()
    val storageError = storageErrorSubject.hide().map { it as Error }

    val discoverUiState = Observable.combineLatest(
        discoverPageLoadingSubject,
        animalTypesLoadingSubject,
        paginatedAnimalsSubject,
        animalTypesSubject
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

    fun navigateToSearch(typeName: String = "") {
        discoverEventSubject.onNext(DiscoverEvent.NavigateToSearch(typeName))
    }

    override fun navigateToAnimalDetails(id: Long) {
        runAnimalDetailsAction(id)
    }

    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionWithId = { actionId ->
            val animals = paginatedAnimalsSubject.value?.animals ?: throw Exception()
            val foundedAnimal =
                animals.find { animal -> animal.id == actionId } ?: throw Exception()
            val media = foundedAnimal.media
            runGalleryAction(media)
        })
    }


    init {
        observeDiscoverPage()
        observeAnimalTypes()
        requestData()
    }

    private fun observeDiscoverPage() {
        getDiscoverPaginatedAnimalsUseCase()
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess {
                        paginatedAnimalsSubject.onNext(it.success)
                    }
                }
            }.addTo(subscriptions)
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

    fun requestData() {
        requestDiscoverPage()
        requestAnimalTypes()
    }

    private fun requestDiscoverPage() {
        requestDiscoverPageUseCase(
            onLoading = { loading ->
                discoverPageLoadingSubject.onNext(loading)
            })
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                result
                    .onNetworkError(onNetworkError = { networkError ->
                        networkErrorSubject.onNext(networkError)
                    })
                    .onStorageError(onStorageError = { storageError ->
                        storageErrorSubject.onNext(storageError)
                    })
            }.addTo(subscriptions)
    }

    private fun requestAnimalTypes() {
        requestAnimalTypesUseCase(
            onLoading = { loading ->
                animalTypesLoadingSubject.onNext(loading)
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

    override fun onCleared() {
        subscriptions.dispose()
    }

}

