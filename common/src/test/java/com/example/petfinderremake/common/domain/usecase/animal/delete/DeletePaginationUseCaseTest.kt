package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DeletePaginationUseCaseTest : AnimalRepositoryTest() {

    private lateinit var deletePaginationUseCase: DeletePaginationUseCase
    private val pagination = Pagination.initPagination.copy(100, 5, 99)

    @Before
    fun setup() {
        deletePaginationUseCase = DeletePaginationUseCase(animalRepository)
        animalRepository.storePagination(pagination)
    }

    @Test
    fun `when delete pagination, then pagination is default`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deletePaginationUseCase().subscribe(testObserver)

        //then
        val storedPagination = animalRepository.getPagination()
        val testObserver2 = TestObserver<Pagination>()
        storedPagination.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEqualTo(Pagination.initPagination)
    }

    @Test
    fun `when delete pagination, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deletePaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when delete pagination, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deletePaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when delete pagination, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deletePaginationUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }
}