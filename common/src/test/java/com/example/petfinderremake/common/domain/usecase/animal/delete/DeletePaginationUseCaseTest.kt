package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeletePaginationUseCaseTest : AnimalRepositoryTest(){

    private lateinit var deletePaginationUseCase: DeletePaginationUseCase
    private val pagination = Pagination.initPagination.copy(100, 5, 99)

    @Before
    fun setup() {
        deletePaginationUseCase = DeletePaginationUseCase(animalRepository)

        runBlocking {
            animalRepository.storePagination(pagination)
        }
    }

    @Test
    fun `when delete pagination, then pagination is default`() {
        runBlocking {

            //when
            deletePaginationUseCase()

            //then
            val storedPagination = animalRepository.getPagination().first()
            Truth.assertThat(storedPagination).isEqualTo(Pagination.initPagination)
        }
    }

    @Test
    fun `when delete pagination, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = deletePaginationUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when delete pagination, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = deletePaginationUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when delete pagination, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = deletePaginationUseCase()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }
}