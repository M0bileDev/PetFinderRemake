package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAnimalRepository : AnimalRepository {

    private val fakeAnimals = mutableListOf<AnimalWithDetails>()
    private val fakeAnimalTypes = mutableListOf<AnimalType>()
    private val fakeAnimalBreeds = mutableListOf<AnimalBreeds>()
    private var fakePagination = Pagination.initPagination
    private var fakeDiscoverPagination = PaginatedAnimals.initPaginatedAnimals

    override suspend fun requestAnimalsPage(
        animalParameters: AnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): PaginatedAnimals {
        return PaginatedAnimals.initPaginatedAnimals
    }

    override suspend fun requestAnimal(id: Long): AnimalWithDetails {
        return AnimalWithDetails.noAnimalWithDetails
    }

    override suspend fun requestAnimalTypes(): List<AnimalType> {
        return listOf(AnimalType(emptyList(), emptyList(), emptyList(), "Type1"), AnimalType(emptyList(), emptyList(), emptyList(), "Type2"))
    }

    override suspend fun requestAnimalType(type: String): AnimalType {
        return AnimalType(emptyList(), emptyList(), emptyList(), "Type1")
    }

    override suspend fun requestAnimalBreeds(type: String): List<AnimalBreeds> {
        return listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"),AnimalBreeds("Breed3"))
    }

    override suspend fun requestDiscoverPage(): PaginatedAnimals {
        return PaginatedAnimals.initPaginatedAnimals
    }

    override suspend fun storeAnimals(animals: List<AnimalWithDetails>) {
        fakeAnimals.addAll(animals)
    }

    override suspend fun storePagination(pagination: Pagination) {
        fakePagination = pagination
    }

    override suspend fun storeAnimalTypes(types: List<AnimalType>) {
        fakeAnimalTypes.addAll(types)
    }

    override suspend fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>) {
        fakeAnimalBreeds.addAll(animalBreeds)
    }

    override suspend fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals) {
        fakeDiscoverPagination = paginatedAnimals
    }

    override fun getAnimals(): Flow<List<AnimalWithDetails>> {
        return flow { emit(fakeAnimals) }
    }

    override fun getPagination(): Flow<Pagination> {
        return flow { emit(fakePagination) }
    }

    override fun getAnimal(id: Long): Flow<AnimalWithDetails> {
        return flow { emit(fakeAnimals.first { it.id == id }) }
    }

    override fun getAnimalTypes(): Flow<List<AnimalType>> {
        return flow { emit(fakeAnimalTypes) }
    }

    override fun getAnimalType(type: String): Flow<AnimalType> {
        return flow { emit(fakeAnimalTypes.first { it.name == type }) }
    }

    override fun getAnimalBreeds(): Flow<List<AnimalBreeds>> {
        return flow { emit(fakeAnimalBreeds) }
    }

    override fun getDiscoverPaginatedAnimals(): Flow<PaginatedAnimals> {
        return flow { emit(fakeDiscoverPagination) }
    }

    override suspend fun deleteAnimals() {
        fakeAnimals.clear()
    }

    override suspend fun deleteBreeds() {
        fakeAnimalBreeds.clear()
    }

    override suspend fun deletePagination() {
        fakePagination = Pagination.initPagination
    }

}