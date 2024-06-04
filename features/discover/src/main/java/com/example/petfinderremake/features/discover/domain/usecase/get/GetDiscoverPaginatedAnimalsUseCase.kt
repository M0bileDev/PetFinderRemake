package com.example.petfinderremake.features.discover.domain.usecase.get

import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDiscoverPaginatedAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(): Flow<Result<PaginatedAnimals, NotYetDefinedError>> {
        return animalRepository.getDiscoverPaginatedAnimals().map { data ->
            Result.Success(data)
        }
    }
}