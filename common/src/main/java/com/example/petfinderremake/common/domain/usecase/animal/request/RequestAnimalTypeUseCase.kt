package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalTypeUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(
        type: String,
        onLoading: (Boolean) -> Unit
    ): Observable<Result<Unit, RootError>> {

        return if (type.isEmpty()) {
            Observable
                .just<Result<Unit, RootError>>(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY))
                .subscribeOn(Schedulers.io())
        } else {
            val apiResult = animalRepository.requestAnimalType(type)
            apiResult
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { onLoading(true) }
                .map<Result<Unit, RootError>> { data ->
                    animalRepository.storeAnimalTypes(listOf(data))
                    Result.Success(Unit)
                }.onErrorResumeNext { error ->
                    if (error is HttpException) {
                        val networkError =
                            NetworkError.entries.find { networkError -> networkError.code == error.code() }
                                ?: throw NetworkError.NetworkErrorTypeException()
                        val result = Result.Error(networkError)
                        Observable.just(result)
                    } else {
                        throw Exception()
                    }
                }.doOnNext {
                    onLoading(false)
                }
        }
    }
}