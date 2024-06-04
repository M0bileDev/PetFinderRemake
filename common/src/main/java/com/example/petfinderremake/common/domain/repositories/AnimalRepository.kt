package com.example.petfinderremake.common.domain.repositories

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {

    suspend fun requestAnimalsPage(
        animalParameters: AnimalParameters = AnimalParameters.noAnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): PaginatedAnimals

    suspend fun requestAnimal(id: Long): AnimalWithDetails
    suspend fun requestAnimalTypes(): List<AnimalType>
    suspend fun requestAnimalType(type: String): AnimalType
    suspend fun requestAnimalBreeds(type: String): List<AnimalBreeds>
    suspend fun requestDiscoverPage(): PaginatedAnimals

    suspend fun storeAnimals(animals: List<AnimalWithDetails>)
    suspend fun storePagination(pagination: Pagination)
    suspend fun storeAnimalTypes(types: List<AnimalType>)
    suspend fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>)
    suspend fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals)


    fun getAnimals(): Flow<List<AnimalWithDetails>>
    fun getPagination(): Flow<Pagination>
    fun getAnimal(id: Long): Flow<AnimalWithDetails>
    fun getAnimalTypes(): Flow<List<AnimalType>>
    fun getAnimalType(type: String): Flow<AnimalType>
    fun getAnimalBreeds(): Flow<List<AnimalBreeds>>
    fun getDiscoverPaginatedAnimals(): Flow<PaginatedAnimals>

    suspend fun deleteAnimals()
    suspend fun deleteBreeds()
    suspend fun deletePagination()
}

