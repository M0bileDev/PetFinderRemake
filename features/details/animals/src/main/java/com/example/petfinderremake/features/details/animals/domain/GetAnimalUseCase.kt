package com.example.petfinderremake.features.details.animals.domain

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(id: Long): Observable<Result<AnimalWithDetails, NotYetDefinedError>> {
        return animalRepository
            .getAnimal(id)
            .subscribeOn(Schedulers.io())
            .map { animal ->
                Result.Success(animal)
            }
    }
}