package com.example.petfinderremake.features.details.animals.domain

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(id: Long): Flow<Result<AnimalWithDetails, NotYetDefinedError>> {
        return animalRepository.getAnimal(id).map { animal ->
            Result.Success(animal)
        }
    }
}