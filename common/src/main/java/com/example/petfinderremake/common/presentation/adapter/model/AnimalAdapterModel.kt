package com.example.petfinderremake.common.presentation.adapter.model

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.animal.details.Breed
import com.example.petfinderremake.common.domain.model.animal.media.Media
import java.time.LocalDateTime

sealed class AnimalAdapterModel(val adapterId: Long, val adapterViewType: AnimalAdapterEnum) {

    data class GridModel(
        val id: Long,
        val name: String,
        val age: String,
        val breed: Breed,
        val media: Media,
        val viewType: AnimalAdapterEnum = AnimalAdapterEnum.ANIMAL_GRID_ITEM
    ) : AnimalAdapterModel(id, viewType)

    data class GridModelVertical(
        val id: Long,
        val name: String,
        val age: String,
        val breed: Breed,
        val media: Media,
        val viewType: AnimalAdapterEnum = AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM
    ) : AnimalAdapterModel(id, viewType)

    data class LinearModel(
        val id: Long,
        val name: String,
        val age: String,
        val breed: Breed,
        val media: Media,
        val tags: List<String>,
        val publishedAt: LocalDateTime,
        val viewType: AnimalAdapterEnum = AnimalAdapterEnum.ANIMAL_ITEM_LINEAR
    ) : AnimalAdapterModel(id, viewType)

    data class LoadMore(val totalCount: Int) :
        AnimalAdapterModel(0L, AnimalAdapterEnum.LOAD_MORE)
}


enum class AnimalAdapterEnum {
    ANIMAL_ITEM_LINEAR,
    ANIMAL_GRID_ITEM,
    ANIMAL_GRID_VERTICAL_ITEM,
    LOAD_MORE
}

fun List<AnimalWithDetails>.createAnimalAdapterModel(animalAdapterEnum: AnimalAdapterEnum) =
    when (animalAdapterEnum) {
        AnimalAdapterEnum.ANIMAL_ITEM_LINEAR -> {
            emptyList<AnimalAdapterModel.LinearModel>()
        }

        AnimalAdapterEnum.ANIMAL_GRID_ITEM -> {
            map {
                AnimalAdapterModel.GridModel(
                    it.id,
                    it.name,
                    it.age,
                    it.details.breed,
                    it.media
                )
            }
        }

        AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM -> {
            map {
                AnimalAdapterModel.GridModelVertical(
                    it.id,
                    it.name,
                    it.age,
                    it.details.breed,
                    it.media
                )
            }
        }

        AnimalAdapterEnum.LOAD_MORE -> {
            emptyList<AnimalAdapterModel.LoadMore>()
        }
    }


fun List<AnimalAdapterModel>.addLoadMore(totalCount: Int): List<AnimalAdapterModel> {
    val updatedList = this.toMutableList()
    updatedList.add(AnimalAdapterModel.LoadMore(totalCount))
    return updatedList.toList()
}


