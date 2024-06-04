package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.ext.isNegative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RequestSingleAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    suspend operator fun invoke(
        id: Long,
        onLoading: (Boolean) -> Unit
    ): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {

            onLoading(true)
            val animal: AnimalWithDetails

            if (id.isNegative()) {
                onLoading(false)
                return@withContext Result.Error(ArgumentError.ARGUMENT_IS_NEGATIVE)
            }

            try {
                animal = animalRepository.requestAnimal(id)
            } catch (exception: HttpException) {
                onLoading(false)
                val networkError =
                    NetworkError.entries.find { it.code == exception.code() }
                        ?: throw NetworkError.NetworkErrorTypeException()
                return@withContext Result.Error(networkError)
            }

            animalRepository.storeAnimals(listOf(animal))
                .run { onLoading(false) }
                .run { Result.Success(Unit) }
        }
    }
}