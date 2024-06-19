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
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PetFinderAnimalRepository @Inject constructor(
    private val animalStorage: AnimalStorage,
    private val petFinderApi: PetFinderApi,
    private val apiAnimalMapper: ApiAnimalMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val apiTypesMapper: ApiTypesMapper,
    private val apiAnimalBreedsMapper: ApiAnimalBreedsMapper
) : AnimalRepository {

    override fun requestAnimalsPage(
        animalParameters: AnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): Observable<PaginatedAnimals> {

        return with(animalParameters) {
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
            ).map { apiPaginatedAnimals ->
                val (apiAnimals, apiPagination) = apiPaginatedAnimals
                PaginatedAnimals(
                    apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
                    apiPaginationMapper.mapToDomain(apiPagination)
                )
            }
        }

    }

    override fun requestAnimal(id: Long): Observable<AnimalWithDetails> {

        return petFinderApi.getAnimal(id)
            .map { apiAnimal -> apiAnimalMapper.mapToDomain(apiAnimal) }
    }

    override
    fun requestAnimalTypes(): Observable<List<AnimalType>> {

        return petFinderApi.getAnimalTypes()
            .map { apiTypes -> apiTypes.types?.map { apiTypesMapper.mapToDomain(it) }.orEmpty() }
    }

    override fun requestAnimalType(type: String): Observable<AnimalType> {

        return petFinderApi.getAnimalType(type)
            .map { apiType -> apiTypesMapper.mapToDomain(apiType) }
    }

    override
    fun requestAnimalBreeds(type: String): Observable<List<AnimalBreeds>> {

        return petFinderApi.getAnimalBreeds(type).map { apiAnimalBreeds ->
            apiAnimalBreeds.breeds?.map {
                apiAnimalBreedsMapper.mapToDomain(it)
            }.orEmpty()
        }
    }

    override fun requestDiscoverPage(): Observable<PaginatedAnimals> {

        return petFinderApi.getAnimalsPage(
            pageToLoad = 1,
            pageSize = 20
        ).map { apiPaginatedAnimals ->
            val (apiAnimals, apiPagination) = apiPaginatedAnimals
            PaginatedAnimals(
                apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
                apiPaginationMapper.mapToDomain(apiPagination)
            )
        }
    }

    override fun storeAnimals(animals: List<AnimalWithDetails>): Completable {
        return animalStorage.storeAnimals(animals)
    }

    override fun storePagination(pagination: Pagination): Completable {
        return animalStorage.storePagination(pagination)
    }

    override fun storeAnimalTypes(types: List<AnimalType>): Completable {
        return animalStorage.storeAnimalTypes(types)
    }

    override fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>): Completable {
        return animalStorage.storeAnimalBreeds(animalBreeds)
    }

    override fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals): Completable {
        return animalStorage.storeDiscoverPaginatedAnimals(paginatedAnimals)
    }

    override fun getAnimals(): Observable<List<AnimalWithDetails>> {
        return animalStorage.getAnimals()
    }

    override fun getPagination(): Observable<Pagination> {
        return animalStorage.getPagination()
    }

    override fun getAnimal(id: Long): Observable<AnimalWithDetails> {
        return animalStorage.getAnimal(id)
    }

    override fun getAnimalTypes(): Observable<List<AnimalType>> {
        return animalStorage.getAnimalTypes()
    }

    override fun getAnimalType(type: String): Observable<AnimalType> {
        return animalStorage.getAnimalType(type)
    }

    override fun getAnimalBreeds(): Observable<List<AnimalBreeds>> {
        return animalStorage.getAnimalBreeds()
    }

    override fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals> {
        return animalStorage.getDiscoverPaginatedAnimals()
    }

    override fun deleteAnimals(): Completable {
        return animalStorage.deleteAnimals()
    }

    override fun deleteBreeds(): Completable {
        return animalStorage.deleteBreeds()
    }

    override fun deletePagination(): Completable {
        return animalStorage.deletePagination()
    }

}