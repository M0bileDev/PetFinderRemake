package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPaginationUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(): Flow<Result<Pagination, NotYetDefinedError>> {
        return animalRepository.getPagination().map { data ->
            Result.Success(data)
        }
    }
}