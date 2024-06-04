package com.example.petfinderremake.features.details.animals.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.result.onSuccess
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender.GalleryArg.Companion.runAction
import com.example.petfinderremake.features.details.animals.domain.GetAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalDetailsViewModel @Inject constructor(
    private val getAnimalUseCase: GetAnimalUseCase
) : ViewModel(), GallerySender {

    override val gallerySenderEvent: Channel<GallerySender.SenderEvent> = Channel()

    private var gallerySenderJob: Job? = null
    private var getAnimalJob: Job? = null

    private var _detailsUiState = MutableStateFlow(DetailsUiState.noDetailsUiState)
    val detailsUiState = _detailsUiState.asStateFlow()

    fun setupArgs(idArgs: Long) {
        getAnimal(idArgs)
    }

    private fun getAnimal(id: Long) {
        getAnimalJob?.cancel()
        getAnimalJob = viewModelScope.launch {
            getAnimalUseCase(id).collectLatest { result ->
                with(result) {
                    onSuccess { result ->
                        _detailsUiState.update { previousState ->
                            previousState.copy(
                                animalWithDetails = result.success
                            )
                        }
                    }
                }
            }
        }
    }


    override fun navigateToGallery(galleryArg: GallerySender.GalleryArg) {
        galleryArg.runAction(actionNoArg = {
            val animal = detailsUiState.value
            val media = animal.media
            gallerySenderJob?.cancel()
            gallerySenderJob = viewModelScope.runGalleryAction(media)
        })
    }

    override fun onCleared() {
        gallerySenderJob?.cancel()
        getAnimalJob?.cancel()
    }
}