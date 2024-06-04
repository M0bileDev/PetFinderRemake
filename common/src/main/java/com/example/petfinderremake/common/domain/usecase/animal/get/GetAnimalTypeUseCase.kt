package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAnimalTypeUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(type: String): Flow<Result<AnimalType, ArgumentError>> {

        if (type.isEmpty()) {
            return flow { emit(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY)) }
        }

        return animalRepository.getAnimalType(type).map { data ->
            Result.Success(data)
        }
    }
}