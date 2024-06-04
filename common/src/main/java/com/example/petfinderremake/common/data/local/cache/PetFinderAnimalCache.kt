package com.example.petfinderremake.common.data.local.cache

import com.example.petfinderremake.common.domain.local.AnimalStorage
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PetFinderAnimalCache @Inject constructor() : AnimalStorage {

    private var discoverPaginatedAnimalsCache =
        MutableStateFlow(PaginatedAnimals.initPaginatedAnimals)
    private var animalsCache = MutableStateFlow(listOf<AnimalWithDetails>())
    private var paginationCache = MutableStateFlow(Pagination.initPagination)
    private var animalTypesCache = MutableStateFlow(listOf<AnimalType>())
    private var animalBreedsCache = MutableStateFlow(listOf<AnimalBreeds>())
    private val allAnimals = combine(
        animalsCache,
        discoverPaginatedAnimalsCache
    ) { animalsCache, discoverPaginatedAnimalsCache ->
        val allAnimals = mutableListOf<AnimalWithDetails>()
        val discoverAnimalsCache = discoverPaginatedAnimalsCache.animals
        allAnimals.addAll(animalsCache)
        allAnimals.addAll(discoverAnimalsCache)
        allAnimals
    }

    override suspend fun storeAnimals(animals: List<AnimalWithDetails>) {
        val updatedAnimals = animalsCache.value.toMutableList()
        updatedAnimals.addAll(animals)
        animalsCache.value = updatedAnimals.toList()
    }

    override suspend fun storePagination(pagination: Pagination) {
        paginationCache.value = pagination
    }

    override suspend fun storeAnimalTypes(types: List<AnimalType>) {
        animalTypesCache.value = types
    }

    override suspend fun storeAnimalBreeds(breeds: List<AnimalBreeds>) {
        animalBreedsCache.value = breeds
    }

    override suspend fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals) =
        with(paginatedAnimals) {
            discoverPaginatedAnimalsCache.update { previousState ->
                previousState.copy(
                    animals = animals,
                    pagination = pagination
                )
            }
        }

    override fun getAnimals(): Flow<List<AnimalWithDetails>> {
        return animalsCache.asStateFlow()
    }

    override fun getPagination(): Flow<Pagination> {
        return paginationCache.asStateFlow()
    }


    override fun getAnimal(id: Long): Flow<AnimalWithDetails> {
        return allAnimals.map { it.first { it.id == id } }
    }

    override fun getAnimalTypes(): Flow<List<AnimalType>> {
        return animalTypesCache.asStateFlow()
    }

    override fun getAnimalType(type: String): Flow<AnimalType> {
        return animalTypesCache.asStateFlow()
            .transform { it.first { animalType -> animalType.name == type } }
    }

    override fun getAnimalBreeds(): Flow<List<AnimalBreeds>> {
        return animalBreedsCache.asStateFlow()
    }

    override fun getDiscoverPaginatedAnimals(): Flow<PaginatedAnimals> {
        return discoverPaginatedAnimalsCache.asStateFlow()
    }

    override suspend fun deleteAnimals() {
        animalsCache.value = emptyList()
    }

    override suspend fun deleteBreeds() {
        animalBreedsCache.value = emptyList()
    }

    override suspend fun deletePagination() = with(Pagination.initPagination) {
        paginationCache.update { previousState ->
            previousState.copy(
                totalCount = totalCount, currentPage = currentPage
            )
        }
    }

}