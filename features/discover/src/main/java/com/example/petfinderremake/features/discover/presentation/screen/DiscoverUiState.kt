package com.example.petfinderremake.features.discover.presentation.screen

import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterModel
import com.example.petfinderremake.common.domain.model.animal.AnimalType

data class DiscoverUiState(
    val totalCount: Int = -1,
    val adapterModels: List<AnimalAdapterModel> = emptyList(),
    val isTopLoading: Boolean = false,
    val isBottomLoading: Boolean= false,
    val animalTypes: List<AnimalType> = emptyList(),
) {
    val allLoadingsFinished get() = !isBottomLoading && !isTopLoading

    companion object{
        val noDiscoverUiState = DiscoverUiState()
    }
}