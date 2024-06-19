package com.example.petfinderremake.common.domain.usecase.animal.request

import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalTypesUseCase @Inject constructor(
    private val animalRepository: AnimalRepository
) {

    operator fun invoke(
        onLoading: (Boolean) -> Unit,
    ): Observable<Result<Unit, RootError>> {

        val apiResult = animalRepository.requestAnimalTypes()
        return apiResult
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { onLoading(true) }
            .map<Result<Unit, RootError>> { data ->
                when {
                    data.isEmpty() -> {
                        Result.Error(StorageError.NO_DATA_TO_STORE)
                    }

                    else -> {
                        with(animalRepository) {
                            animalRepository.storeAnimalTypes(data)
                            Result.Success(Unit)
                        }
                    }
                }
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
