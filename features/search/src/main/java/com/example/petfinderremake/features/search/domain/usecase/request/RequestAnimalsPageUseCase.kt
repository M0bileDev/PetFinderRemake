package com.example.petfinderremake.features.search.domain.usecase.request

import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.repositories.AnimalRepository
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.NetworkError
import com.example.petfinderremake.common.domain.result.error.StorageError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class RequestAnimalsPageUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
) {
    operator fun invoke(
        animalParameters: AnimalParameters = AnimalParameters.noAnimalParameters,
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE,
        onLoading: (Boolean) -> Unit,
    ): Observable<Result<Unit, RootError>> {

        return animalRepository
            .requestAnimalsPage(animalParameters, pageToLoad, pageSize)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                onLoading(true)
            }
            .flatMap { data ->
                Observable.fromCallable<Result<Unit, RootError>> {
                    when {
                        data.animals.isEmpty() -> {
                            Result.Error(StorageError.NO_DATA_TO_STORE)
                        }

                        else -> {
                            with(animalRepository) {

                                val (animals, pagination) = data

                                storeAnimals(animals)
                                storePagination(pagination)
                                Result.Success(Unit)
                            }
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

