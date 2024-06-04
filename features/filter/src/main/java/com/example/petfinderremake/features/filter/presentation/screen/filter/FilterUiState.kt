package com.example.petfinderremake.features.filter.presentation.screen.filter

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType

data class FilterUiState(
    val isSelected: Boolean = false,
    val animalTypes: List<AnimalType> = emptyList(),
    val animalBreeds: List<AnimalBreeds> = emptyList(),
    val animalTypesLoading: Boolean = false,
    val animalBreedsLoading: Boolean = false
) {
    val isLoading = animalTypesLoading || animalBreedsLoading
    val isFilerTypeButtonEnable = !animalTypesLoading && animalTypes.isNotEmpty()
    val isFilterBreedButtonEnabled = !animalBreedsLoading && animalBreeds.isNotEmpty()
}



