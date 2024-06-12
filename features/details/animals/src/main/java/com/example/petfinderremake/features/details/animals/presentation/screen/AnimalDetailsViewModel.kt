package com.example.petfinderremake.features.details.animals.presentation.screen

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.ext.getValueOrThrow
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender.GalleryArg.Companion.runAction
import com.example.petfinderremake.features.details.animals.domain.GetAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class AnimalDetailsViewModel @Inject constructor(
    private val getAnimalUseCase: GetAnimalUseCase
) : ViewModel(), GallerySender {

    private val subscriptions = CompositeDisposable()
    override val gallerySenderSubject = PublishSubject.create<GallerySender.SenderEvent>()

    private val detailsUiStateSubject = BehaviorSubject.createDefault(DetailsUiState.noDetailsUiState)
    val detailsUiState = detailsUiStateSubject.hide()

    fun setupArgs(idArgs: Long) {
        getAnimal(idArgs)
    }

    private fun getAnimal(id: Long) {
        getAnimalUseCase(id)
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                with(result) {
                    onSuccess { result ->
                        detailsUiStateSubject.onNext(DetailsUiState(result.success))
                    }
                }
            }.addTo(subscriptions)

    }


    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionNoArg = {
            val animal = detailsUiStateSubject.getValueOrThrow()
            val media = animal.media
            runGalleryAction(media)
        })
    }

    override fun onCleared() {
        subscriptions.dispose()
    }
}