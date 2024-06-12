package com.example.petfinderremake.common.domain.local

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import io.reactivex.rxjava3.core.Observable

interface AnimalStorage {

    fun storeAnimals(animals: List<AnimalWithDetails>)
    fun storePagination(pagination: Pagination)
    fun storeAnimalTypes(types: List<AnimalType>)
    fun storeAnimalBreeds(breeds: List<AnimalBreeds>)
    fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals)

    fun deleteAnimals()
    fun deletePagination()
    fun deleteBreeds()

    fun getAnimals(): Observable<List<AnimalWithDetails>>
    fun getPagination(): Observable<Pagination>
    fun getAnimal(id: Long): Observable<AnimalWithDetails>
    fun getAnimalTypes(): Observable<List<AnimalType>>
    fun getAnimalType(type: String): Observable<AnimalType>
    fun getAnimalBreeds(): Observable<List<AnimalBreeds>>
    fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals>
}