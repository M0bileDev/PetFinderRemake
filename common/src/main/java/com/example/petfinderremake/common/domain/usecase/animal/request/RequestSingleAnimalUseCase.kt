package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.ext.isNegative
import io.reactivex.rxjava3.core.Observable
import retrofit2.HttpException
import javax.inject.Inject

class RequestSingleAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(
        id: Long,
        onLoading: (Boolean) -> Unit
    ): Observable<Result<Unit, RootError>> {

        return if (id.isNegative()) {
            Observable.just(Result.Error(ArgumentError.ARGUMENT_IS_NEGATIVE))
        } else {
            animalRepository.requestAnimal(id)
                .doOnSubscribe {
                    onLoading(true)
                }
                .flatMap { data ->
                    Observable.fromCallable<Result<Unit, RootError>> {
                        animalRepository.storeAnimals(listOf(data))
                        Result.Success(Unit)
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