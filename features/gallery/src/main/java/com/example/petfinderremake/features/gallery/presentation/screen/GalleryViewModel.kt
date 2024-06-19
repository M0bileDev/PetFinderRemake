package com.example.petfinderremake.features.gallery.presentation.screen

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.ext.getValueOrThrow
import com.example.petfinderremake.common.presentation.navigation.model.GalleryNavArg
import com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum
import com.example.petfinderremake.features.gallery.presentation.model.adapter.createGalleryAdapterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor() : ViewModel() {

    sealed interface GalleryEvent {
        data object NavigateBack : GalleryEvent
    }

    private val galleryEventSubject = PublishSubject.create<GalleryEvent>()
    val galleryEvent = galleryEventSubject
        .hide()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private val galleryUiStateSubject = BehaviorSubject.createDefault(GalleryUiState())
    val galleryUiState = galleryUiStateSubject
        .hide()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun setupArgs(args: GalleryNavArg) {
        val galleryAdapterModel =
            args.images.createGalleryAdapterModel(GalleryAdapterEnum.IMAGE) + args.videos.createGalleryAdapterModel(
                GalleryAdapterEnum.VIDEO
            )
        val uiState = galleryUiStateSubject.getValueOrThrow()
        galleryUiStateSubject.onNext(uiState.copy(galleryAdapterModel = galleryAdapterModel))
    }

    fun navigateBack() {
        galleryEventSubject.onNext(GalleryEvent.NavigateBack)
    }
}