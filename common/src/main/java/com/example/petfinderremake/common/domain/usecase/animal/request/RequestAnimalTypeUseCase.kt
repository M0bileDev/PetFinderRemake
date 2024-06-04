package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalTypeUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    suspend operator fun invoke(
        type: String,
        onLoading: (Boolean) -> Unit
    ): Result<Unit, RootError> {
        return withContext(Dispatchers.IO) {

            onLoading(true)
            val singleType: AnimalType


            if (type.isEmpty()) {
                onLoading(false)
                return@withContext Result.Error(ArgumentError.ARGUMENT_IS_EMPTY)
            }

            try {
                singleType = animalRepository.requestAnimalType(type)
            } catch (exception: HttpException) {
                onLoading(false)
                val networkError =
                    NetworkError.entries.find { it.code == exception.code() }
                        ?: throw NetworkError.NetworkErrorTypeException()
                return@withContext Result.Error(networkError)
            }

            animalRepository.storeAnimalTypes(listOf(singleType))
                .run { onLoading(false) }
                .run { Result.Success(Unit) }
        }
    }
}