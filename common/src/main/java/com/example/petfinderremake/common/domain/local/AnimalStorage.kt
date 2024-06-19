package com.example.petfinderremake.common.domain.local

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface AnimalStorage {

    fun storeAnimals(animals: List<AnimalWithDetails>): Completable
    fun storePagination(pagination: Pagination): Completable
    fun storeAnimalTypes(types: List<AnimalType>): Completable
    fun storeAnimalBreeds(breeds: List<AnimalBreeds>): Completable
    fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals): Completable

    fun deleteAnimals(): Completable
    fun deletePagination(): Completable
    fun deleteBreeds(): Completable

    fun getAnimals(): Observable<List<AnimalWithDetails>>
    fun getPagination(): Observable<Pagination>
    fun getAnimal(id: Long): Observable<AnimalWithDetails>
    fun getAnimalTypes(): Observable<List<AnimalType>>
    fun getAnimalType(type: String): Observable<AnimalType>
    fun getAnimalBreeds(): Observable<List<AnimalBreeds>>
    fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals>
}