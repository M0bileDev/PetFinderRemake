package com.example.petfinderremake.common.data.repositories

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class FakeAnimalRepository : AnimalRepository {

    private val fakeAnimals = mutableListOf<AnimalWithDetails>()
    private val fakeAnimalTypes = mutableListOf<AnimalType>()
    private val fakeAnimalBreeds = mutableListOf<AnimalBreeds>()
    private var fakePagination = Pagination.initPagination
    private var fakeDiscoverPagination = PaginatedAnimals.initPaginatedAnimals

    override fun requestAnimalsPage(
        animalParameters: AnimalParameters,
        pageToLoad: Int,
        pageSize: Int
    ): Observable<PaginatedAnimals> {
        return Observable.just(PaginatedAnimals.initPaginatedAnimals)
    }

    override fun requestAnimal(id: Long): Observable<AnimalWithDetails> {
        return Observable.just(AnimalWithDetails.noAnimalWithDetails)
    }

    override fun requestAnimalTypes(): Observable<List<AnimalType>> {
        return Observable.just(
            listOf(
                AnimalType(emptyList(), emptyList(), emptyList(), "Type1"),
                AnimalType(emptyList(), emptyList(), emptyList(), "Type2")
            )
        )
    }

    override fun requestAnimalType(type: String): Observable<AnimalType> {
        return Observable.just(AnimalType(emptyList(), emptyList(), emptyList(), "Type1"))
    }

    override fun requestAnimalBreeds(type: String): Observable<List<AnimalBreeds>> {
        return Observable.just(
            listOf(
                AnimalBreeds("Breed1"),
                AnimalBreeds("Breed2"),
                AnimalBreeds("Breed3")
            )
        )
    }

    override fun requestDiscoverPage(): Observable<PaginatedAnimals> {
        return Observable.just(PaginatedAnimals.initPaginatedAnimals)
    }

    override fun storeAnimals(animals: List<AnimalWithDetails>): Completable {
        fakeAnimals.addAll(animals)
        return Completable.complete()
    }

    override fun storePagination(pagination: Pagination): Completable {
        fakePagination = pagination
        return Completable.complete()
    }

    override fun storeAnimalTypes(types: List<AnimalType>): Completable {
        fakeAnimalTypes.addAll(types)
        return Completable.complete()
    }

    override fun storeAnimalBreeds(animalBreeds: List<AnimalBreeds>): Completable {
        fakeAnimalBreeds.addAll(animalBreeds)
        return Completable.complete()
    }

    override fun storeDiscoverPaginatedAnimals(paginatedAnimals: PaginatedAnimals): Completable {
        fakeDiscoverPagination = paginatedAnimals
        return Completable.complete()
    }

    override fun getAnimals(): Observable<List<AnimalWithDetails>> {
        return Observable.just(fakeAnimals)
    }

    override fun getPagination(): Observable<Pagination> {
        return Observable.just(fakePagination)
    }

    override fun getAnimal(id: Long): Observable<AnimalWithDetails> {
        return Observable.just(fakeAnimals.first { it.id == id })
    }

    override fun getAnimalTypes(): Observable<List<AnimalType>> {
        return Observable.just(fakeAnimalTypes)
    }

    override fun getAnimalType(type: String): Observable<AnimalType> {
        return Observable.just(fakeAnimalTypes.first { it.name == type })
    }

    override fun getAnimalBreeds(): Observable<List<AnimalBreeds>> {
        return Observable.just(fakeAnimalBreeds)
    }

    override fun getDiscoverPaginatedAnimals(): Observable<PaginatedAnimals> {
        return Observable.just(fakeDiscoverPagination)
    }

    override fun deleteAnimals(): Completable {
        fakeAnimals.clear()
        return Completable.complete()
    }

    override fun deleteBreeds(): Completable {
        fakeAnimalBreeds.clear()
        return Completable.complete()
    }

    override fun deletePagination(): Completable {
        fakePagination = Pagination.initPagination
        return Completable.complete()
    }

}