package com.example.petfinderremake.features.discover.domain.usecase.get

import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.features.discover.AnimalRepositoryTest
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetDiscoverPaginatedAnimalsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase
    private val paginatedAnimals = PaginatedAnimals.initPaginatedAnimals

    @Before
    fun setup() {
        getDiscoverPaginatedAnimalsUseCase = GetDiscoverPaginatedAnimalsUseCase(animalRepository)
        animalRepository.storeDiscoverPaginatedAnimals(
            paginatedAnimals
        )
    }

    @Test
    fun `when get discover paginated animals, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<PaginatedAnimals, NotYetDefinedError>>()
        val result = getDiscoverPaginatedAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get discover paginated animals, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<PaginatedAnimals, NotYetDefinedError>>()
        val result = getDiscoverPaginatedAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get discover paginated animals with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<PaginatedAnimals, NotYetDefinedError>>()
        val result = getDiscoverPaginatedAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(paginatedAnimals))
    }
}