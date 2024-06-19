package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetAnimalTypeUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    operator fun invoke(type: String): Observable<Result<AnimalType, ArgumentError>> {

        return if (type.isEmpty()) {
            Observable
                .just<Result<AnimalType, ArgumentError>>(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY))
                .subscribeOn(Schedulers.io())
        } else {
            animalRepository
                .getAnimalType(type)
                .subscribeOn(Schedulers.io())
                .map { data ->
                    Result.Success(data)
                }
        }
    }
}