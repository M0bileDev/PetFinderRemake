package com.example.petfinderremake.features.discover.domain.usecase.get

import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetDiscoverPaginatedAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(): Observable<Result<PaginatedAnimals, NotYetDefinedError>> {
        return animalRepository
            .getDiscoverPaginatedAnimals()
            .subscribeOn(Schedulers.io())
            .map { data ->
                Result.Success(data)
            }
    }
}