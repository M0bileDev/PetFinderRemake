package com.example.petfinderremake.features.search.presentation.screen

import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterModel

data class SearchUiState(
    val adapterModels: List<AnimalAdapterModel>,
    val isLoadInitPage: Boolean,
    val isLoadMore: Boolean,
    val isFilterActive: Boolean
) {
    val isLoading = isLoadInitPage || isLoadMore
}

