package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DeleteAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {
    operator fun invoke(): Observable<Result<Unit, NotYetDefinedError>> {
        return Observable.create {
            animalRepository.deleteAnimals()
                .run { Result.Success(Unit) }
        }
    }
}