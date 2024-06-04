package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.pagination.Pagination
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPaginationUseCaseTest : AnimalRepositoryTest() {
    
    private lateinit var getPaginationUseCase: GetPaginationUseCase
    private val pagination = Pagination.initPagination.copy(100, 5, 99)
    
    @Before
    fun setup(){
        getPaginationUseCase = GetPaginationUseCase(animalRepository)

        runBlocking {
            animalRepository.storePagination(pagination)
        }
    }

    @Test
    fun `when get pagination, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getPaginationUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get pagination, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getPaginationUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get pagination with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getPaginationUseCase().first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(pagination))

        }
    }
}