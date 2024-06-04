package com.example.petfinderremake.common.domain.local

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import kotlinx.coroutines.flow.Flow

interface AnimalStorage {

    suspend fun storeAnimals(animals: List<AnimalWithDetails>)
    suspend fun storePagination(pagination: Pagination)
    suspend fun storeAnimalTypes(types: List<AnimalType>)
    suspend fun storeAnimalBreeds(breeds: List<AnimalBreeds>)
    suspend fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals)

    suspend fun deleteAnimals()
    suspend fun deletePagination()
    suspend fun deleteBreeds()

    fun getAnimals(): Flow<List<AnimalWithDetails>>
    fun getPagination(): Flow<Pagination>
    fun getAnimal(id: Long): Flow<AnimalWithDetails>
    fun getAnimalTypes(): Flow<List<AnimalType>>
    fun getAnimalType(type: String): Flow<AnimalType>
    fun getAnimalBreeds(): Flow<List<AnimalBreeds>>
    fun getDiscoverPaginatedAnimals() : Flow<PaginatedAnimals>
}