package com.example.petfinderremake.common.presentation.utils

import android.content.res.Resources
import com.example.petfinderremake.common.R
import com.example.petfinderremake.common.domain.model.animal.details.Breed

fun getPagingProgress(currentListSize: Int, totalCount: Int): Int {
    return currentListSize / totalCount
}

fun combineAnimalCurrentSizeWithTotalCount(
    currentListSize: Int,
    totalCount: Int,
    resources: Resources
): String {
    return resources.getString(
        R.string.pagination_animal_current_size_of_total_count,
        currentListSize,
        totalCount
    )
}

fun extractBreed(breed: Breed, resources: Resources): String {
    return when {
        breed.unknown -> resources.getString(R.string.breed_unknown)
        breed.mixed -> resources.getString(R.string.breed_mixed)
        else -> breed.primary
    }
}

fun createTagsString(tags: List<String>): String {
    val tagsString = StringBuffer()
    tags.forEachIndexed { index, item ->
        if (index < tags.size - 1) {
            tagsString.append(item).append(", ")
        } else {
            tagsString.append(item)
        }
    }
    return tagsString.toString()
}
