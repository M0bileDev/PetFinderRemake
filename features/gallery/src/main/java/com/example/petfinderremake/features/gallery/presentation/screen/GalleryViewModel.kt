package com.example.petfinderremake.features.gallery.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.presentation.navigation.model.GalleryNavArg
import com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterEnum
import com.example.petfinderremake.features.gallery.presentation.model.adapter.createGalleryAdapterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor() : ViewModel() {

    private var navigateBackJob: Job? = null

    sealed interface GalleryEvent {
        data object NavigateBack : GalleryEvent
    }

    private var _galleryEvent = Channel<GalleryEvent>()
    val galleryEvent get() = _galleryEvent.receiveAsFlow()

    private var _galleryUiState = MutableStateFlow(GalleryUiState())
    val galleryUiState get() = _galleryUiState.asStateFlow()

    fun setupArgs(args: GalleryNavArg) {
        val galleryAdapterModel =
            args.images.createGalleryAdapterModel(GalleryAdapterEnum.IMAGE) + args.videos.createGalleryAdapterModel(
                GalleryAdapterEnum.VIDEO
            )
        _galleryUiState.update { previousState ->
            previousState.copy(
                galleryAdapterModel = galleryAdapterModel
            )
        }
    }

    fun navigateBack() {
        navigateBackJob?.cancel()
        navigateBackJob = viewModelScope.launch {
            _galleryEvent.send(GalleryEvent.NavigateBack)
        }
    }

    override fun onCleared() {
        navigateBackJob?.cancel()
    }
}