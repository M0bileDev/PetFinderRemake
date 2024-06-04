package com.example.petfinderremake.features.discover.domain.usecase.request

import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RequestDiscoverPageUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
) {

    suspend operator fun invoke(
        onLoading: (Boolean) -> Unit,
    ): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {
            onLoading(true)
            val paginatedAnimals: PaginatedAnimals

            try {
                paginatedAnimals = animalRepository.requestDiscoverPage()
            } catch (exception: HttpException) {
                onLoading(false)
                val networkError =
                    NetworkError.entries.find { it.code == exception.code() }
                        ?: throw NetworkError.NetworkErrorTypeException()

                return@withContext Result.Error(networkError)
            }

            if (paginatedAnimals.animals.isEmpty()) {
                onLoading(false)
                return@withContext Result.Error(
                    StorageError.NO_DATA_TO_STORE
                )
            }

            animalRepository.storeDiscoverPaginatedAnimals(paginatedAnimals)
                .run { onLoading(false) }
                .run { Result.Success(Unit) }

        }
    }
}