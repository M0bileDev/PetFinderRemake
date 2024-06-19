package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetAnimalBreedsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(): Observable<Result<List<AnimalBreeds>, NotYetDefinedError>> {
        return animalRepository
            .getAnimalBreeds()
            .subscribeOn(Schedulers.io())
            .map { data ->
                Result.Success(data)
            }
    }
}