package com.example.petfinderremake.features.search.domain.usecase.get

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(): Observable<Result<List<AnimalWithDetails>, NotYetDefinedError>> {
        return animalRepository
            .getAnimals()
            .subscribeOn(Schedulers.io())
            .map { data ->
                Result.Success(data)
            }
    }
}