package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.data.network.api.PetFinderApi
import com.example.petfinderremake.common.data.network.mapper.ApiAnimalBreedsMapper
import com.example.petfinderremake.common.data.network.mapper.ApiAnimalMapper
import com.example.petfinderremake.common.data.network.mapper.ApiPaginationMapper
import com.example.petfinderremake.common.data.network.mapper.ApiTypesMapper
import com.example.petfinderremake.common.domain.local.AnimalStorage
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PetFinderAnimalRepository @Inject constructor(
    private val animalStorage: AnimalStorage,
    private val petFinderApi: PetFinderApi,
    private val apiAnimalMapper: ApiAnimalMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val apiTypesMapper: ApiTypesMapper,
    private val apiAnimalBreedsMapper: ApiAnimalBreedsMapper
) : AnimalRepository {

    override suspend fun requestAnimalsPage(
        animalParameters: AnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): PaginatedAnimals {

        val (apiAnimals, apiPagination) = with(animalParameters) {
            petFinderApi.getAnimalsPage(
                type,
                breed,
                size,
                gender,
                age,
                color,
                coat,
                status,
                name,
                organization,
                goodWithChildren,
                goodWithDogs,
                goodWithCats,
                houseTrained,
                declawed,
                specialNeeds,
                location,
                distance,
                before,
                after,
                sort,
                pageToLoad,
                pageSize
            )
        }

        return PaginatedAnimals(
            apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
            apiPaginationMapper.mapToDomain(apiPagination)
        )
    }

    override suspend fun requestAnimal(id: Long): AnimalWithDetails {

        val apiAnimal = petFinderApi.getAnimal(id)
        return apiAnimalMapper.mapToDomain(apiAnimal)
    }

    override suspend fun requestAnimalTypes(): List<AnimalType> {

        val apiTypes = petFinderApi.getAnimalTypes()
        return apiTypes.types?.map { apiTypesMapper.mapToDomain(it) }.orEmpty()
    }

    override suspend fun requestAnimalType(type: String): AnimalType {

        val apiType = petFinderApi.getAnimalType(type)
        return apiTypesMapper.mapToDomain(apiType)
    }

    override suspend fun requestAnimalBreeds(type: String): List<AnimalBreeds> {

        val apiAnimalBreeds = petFinderApi.getAnimalBreeds(type)
        return apiAnimalBreeds.breeds?.map { apiAnimalBreedsMapper.mapToDomain(it) }.orEmpty()
    }

    override suspend fun requestDiscoverPage(): PaginatedAnimals {
        val (apiAnimals, apiPagination) = petFinderApi.getAnimalsPage(
            pageToLoad = 1,
            pageSize = 20
        )
        return PaginatedAnimals(
            apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
            apiPaginationMapper.mapToDomain(apiPagination)
        )
    }

    override suspend fun storeAnimals(animals: List<AnimalWithDetails>) {
        animalStorage.storeAnimals(animals)
    }

    override suspend fun storePagination(pagination: Pagination) {
        animalStorage.storePagination(pagination)
    }

    override suspend fun storeAnimalTypes(types: List<AnimalType>) {
        animalStorage.storeAnimalTypes(types)
    }

    override suspend fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>) {
        animalStorage.storeAnimalBreeds(animalBreeds)
    }

    override suspend fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals) {
        animalStorage.storeDiscoverPaginatedAnimals(paginatedAnimals)
    }

    override fun getAnimals(): Flow<List<AnimalWithDetails>> {
        return animalStorage.getAnimals()
    }

    override fun getPagination(): Flow<Pagination> {
        return animalStorage.getPagination()
    }

    override fun getAnimal(id: Long): Flow<AnimalWithDetails> {
        return animalStorage.getAnimal(id)
    }

    override fun getAnimalTypes(): Flow<List<AnimalType>> {
        return animalStorage.getAnimalTypes()
    }

    override fun getAnimalType(type: String): Flow<AnimalType> {
        return animalStorage.getAnimalType(type)
    }

    override fun getAnimalBreeds(): Flow<List<AnimalBreeds>> {
        return animalStorage.getAnimalBreeds()
    }

    override fun getDiscoverPaginatedAnimals(): Flow<PaginatedAnimals> {
        return animalStorage.getDiscoverPaginatedAnimals()
    }

    override suspend fun deleteAnimals() {
        animalStorage.deleteAnimals()
    }

    override suspend fun deleteBreeds() {
        animalStorage.deleteBreeds()
    }

    override suspend fun deletePagination() {
        animalStorage.deletePagination()
    }

}