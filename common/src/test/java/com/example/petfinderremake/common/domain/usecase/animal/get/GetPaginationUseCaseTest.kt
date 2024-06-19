package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetPaginationUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getPaginationUseCase: GetPaginationUseCase
    private val pagination = Pagination.initPagination.copy(100, 5, 99)

    @Before
    fun setup() {
        getPaginationUseCase = GetPaginationUseCase(animalRepository)
        animalRepository.storePagination(pagination)
    }

    @Test
    fun `when get pagination, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Pagination, NotYetDefinedError>>()
        val result = getPaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get pagination, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Pagination, NotYetDefinedError>>()
        val result = getPaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get pagination with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<Pagination, NotYetDefinedError>>()
        val result = getPaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(pagination))
    }
}