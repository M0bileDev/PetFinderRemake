package com.example.petfinderremake.features.search.domain.usecase.get

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    suspend operator fun invoke(): Flow<Result<List<AnimalWithDetails>, NotYetDefinedError>> {
        return withContext(Dispatchers.IO) {
            animalRepository.getAnimals().map { data ->
                Result.Success(data)
            }
        }
    }
}