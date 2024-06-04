package com.example.petfinderremake.features.gallery.presentation.screen

import com.example.petfinderremake.features.gallery.presentation.model.adapter.GalleryAdapterModel

data class GalleryUiState(
    val galleryAdapterModel: List<GalleryAdapterModel> = emptyList()
)