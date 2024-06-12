package com.example.petfinderremake.common.data.local.cache

import com.example.petfinderremake.common.domain.local.AnimalStorage
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class PetFinderAnimalCache @Inject constructor() : AnimalStorage {

    private var discoverPaginatedAnimalsCache =
        BehaviorSubject.createDefault(PaginatedAnimals.initPaginatedAnimals)
    private var animalsCache = BehaviorSubject.createDefault(listOf<AnimalWithDetails>())
    private var paginationCache = BehaviorSubject.createDefault(Pagination.initPagination)
    private var animalTypesCache = BehaviorSubject.createDefault(listOf<AnimalType>())
    private var animalBreedsCache = BehaviorSubject.createDefault(listOf<AnimalBreeds>())
    private val allAnimals = Observable.combineLatest(
        animalsCache,
        discoverPaginatedAnimalsCache
    ) { animalsCache, discoverPaginatedAnimalsCache ->
        val allAnimals = mutableListOf<AnimalWithDetails>()
        val discoverAnimalsCache = discoverPaginatedAnimalsCache.animals
        allAnimals.addAll(animalsCache)
        allAnimals.addAll(discoverAnimalsCache)
        allAnimals
    }

    override fun storeAnimals(animals: List<AnimalWithDetails>) {
        val updatedAnimals = animalsCache.value?.toMutableList()
        updatedAnimals?.addAll(animals)
        animalsCache.onNext(updatedAnimals?.toList())
    }

    override fun storePagination(pagination: Pagination) {
        paginationCache.onNext(pagination)
    }

    override fun storeAnimalTypes(types: List<AnimalType>) {
        animalTypesCache.onNext(types)
    }

    override fun storeAnimalBreeds(breeds: List<AnimalBreeds>) {
        animalBreedsCache.onNext(breeds)
    }

    override fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals) {
        discoverPaginatedAnimalsCache.onNext(paginatedAnimals)
    }


    override fun getAnimals(): Observable<List<AnimalWithDetails>> {
        return animalsCache.hide()
    }

    override fun getPagination(): Observable<Pagination> {
        return paginationCache.hide()
    }


    override fun getAnimal(id: Long): Observable<AnimalWithDetails> {
        return allAnimals.map { it.first { it.id == id } }.hide()
    }

    override fun getAnimalTypes(): Observable<List<AnimalType>> {
        return animalTypesCache.hide()
    }

    override fun getAnimalType(type: String): Observable<AnimalType> {
        return animalTypesCache.map { it.first { animalType -> animalType.name == type } }.hide()
    }

    override fun getAnimalBreeds(): Observable<List<AnimalBreeds>> {
        return animalBreedsCache.hide()
    }

    override fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals> {
        return discoverPaginatedAnimalsCache.hide()
    }

    override fun deleteAnimals() {
        animalsCache.onNext(emptyList())
    }

    override fun deleteBreeds() {
        animalBreedsCache.onNext(emptyList())
    }

    override fun deletePagination() {
        paginationCache.onNext(Pagination.initPagination)
    }

}