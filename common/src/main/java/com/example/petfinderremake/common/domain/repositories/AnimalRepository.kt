package com.example.petfinderremake.common.domain.repositories

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface AnimalRepository {

    fun requestAnimalsPage(
        animalParameters: AnimalParameters = AnimalParameters.noAnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): Observable<PaginatedAnimals>

    fun requestAnimal(id: Long): Observable<AnimalWithDetails>
    fun requestAnimalTypes(): Observable<List<AnimalType>>
    fun requestAnimalType(type: String): Observable<AnimalType>
    fun requestAnimalBreeds(type: String): Observable<List<AnimalBreeds>>
    fun requestDiscoverPage(): Observable<PaginatedAnimals>

    fun storeAnimals(animals: List<AnimalWithDetails>) : Completable
    fun storePagination(pagination: Pagination): Completable
    fun storeAnimalTypes(types: List<AnimalType>): Completable
    fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>): Completable
    fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals): Completable


    fun getAnimals(): Observable<List<AnimalWithDetails>>
    fun getPagination(): Observable<Pagination>
    fun getAnimal(id: Long): Observable<AnimalWithDetails>
    fun getAnimalTypes(): Observable<List<AnimalType>>
    fun getAnimalType(type: String): Observable<AnimalType>
    fun getAnimalBreeds(): Observable<List<AnimalBreeds>>
    fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals>

    fun deleteAnimals(): Completable
    fun deleteBreeds(): Completable
    fun deletePagination(): Completable
}

