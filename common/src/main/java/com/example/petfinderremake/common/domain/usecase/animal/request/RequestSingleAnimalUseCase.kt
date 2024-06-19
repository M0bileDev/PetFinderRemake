package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.ext.isNegative
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
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
            Observable
                .just<Result<Unit, RootError>>(Result.Error(ArgumentError.ARGUMENT_IS_NEGATIVE))
                .subscribeOn(Schedulers.io())
        } else {

            val apiResult = animalRepository.requestAnimal(id)
            apiResult
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { onLoading(true) }
                .map<Result<Unit, RootError>> { data ->
                    animalRepository.storeAnimals(listOf(data))
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