package com.example.petfinderremake.features.discover

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.domain.model.animal.media.Photo
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals

object TestUtils {

    val animalWithMedia = AnimalWithDetails.noAnimalWithDetails.copy(
        id = 1L,
        media = Media(photos = listOf(Photo("", "", "")))
    )

    val animalWithNoMedia = AnimalWithDetails.noAnimalWithDetails.copy(
        id = 2L
    )

    val paginationWithAnimals = PaginatedAnimals.initPaginatedAnimals.copy(
        listOf(
            animalWithMedia,
            animalWithNoMedia
        )
    )
}