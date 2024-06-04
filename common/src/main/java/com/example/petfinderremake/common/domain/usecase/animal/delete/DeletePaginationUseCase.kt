package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeletePaginationUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    suspend operator fun invoke(): Result<Unit, NotYetDefinedError> {
        return withContext(Dispatchers.IO) {
            animalRepository.deletePagination()
                .run { Result.Success(Unit) }
        }
    }
}