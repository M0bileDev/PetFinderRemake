package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import io.reactivex.rxjava3.core.Observable
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalBreedsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(
        type: String,
        onLoading: (Boolean) -> Unit,
    ): Observable<Result<Unit, RootError>> {

        return animalRepository.requestAnimalBreeds(type)
            .doOnSubscribe {
                onLoading(true)
            }
            .flatMap { data ->
                Observable.fromCallable<Result<Unit, RootError>> {
                    when {
                        data.isEmpty() -> {
                            Result.Error(StorageError.NO_DATA_TO_STORE)
                        }

                        else -> {
                            with(animalRepository) {
                                deleteBreeds()
                                storeAnimalBreeds(data)
                                Result.Success(Unit)
                            }
                        }
                    }
                }.onErrorResumeWith { error ->
                    if (error is HttpException) {
                        val networkError = NetworkError.entries.find { it.code == error.code() }
                            ?: throw NetworkError.NetworkErrorTypeException()
                        Result.Error(networkError)
                    } else {
                        throw Exception()
                    }
                }.doOnNext {
                    onLoading(false)
                }
            }
    }
}