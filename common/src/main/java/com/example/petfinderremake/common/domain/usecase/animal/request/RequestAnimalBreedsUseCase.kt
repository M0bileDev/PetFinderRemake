package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalBreedsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    suspend operator fun invoke(
        type: String,
        onLoading: (Boolean) -> Unit,
    ): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {

            onLoading(true)
            val animalBreeds: List<AnimalBreeds>

            try {
                animalBreeds = animalRepository.requestAnimalBreeds(type)
            } catch (exception: HttpException) {
                onLoading(false)
                val networkError =
                    NetworkError.entries.find { it.code == exception.code() }
                        ?: throw NetworkError.NetworkErrorTypeException()
                return@withContext Result.Error(networkError)
            }

            if (animalBreeds.isEmpty()) {
                onLoading(false)
                return@withContext Result.Error(StorageError.NO_DATA_TO_STORE)
            }

            animalRepository.deleteBreeds()
                .run { animalRepository.storeAnimalBreeds(animalBreeds) }
                .run { onLoading(false) }
                .run { Result.Success(Unit) }
        }
    }
}