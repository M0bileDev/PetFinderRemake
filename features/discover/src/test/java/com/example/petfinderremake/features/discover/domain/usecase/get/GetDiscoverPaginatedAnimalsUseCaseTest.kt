package com.example.petfinderremake.features.discover.domain.usecase.get

import com.example.petfinderremake.common.domain.model.pagination.PaginatedAnimals
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.features.discover.AnimalRepositoryTest
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetDiscoverPaginatedAnimalsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getDiscoverPaginatedAnimalsUseCase: GetDiscoverPaginatedAnimalsUseCase
    private val paginatedAnimals = PaginatedAnimals.initPaginatedAnimals

    @Before
    fun setup() {
        getDiscoverPaginatedAnimalsUseCase = GetDiscoverPaginatedAnimalsUseCase(animalRepository)

        runBlocking {
            animalRepository.storeDiscoverPaginatedAnimals(
                paginatedAnimals
            )
        }
    }

    @Test
    fun `when get discover paginated animals, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getDiscoverPaginatedAnimalsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get discover paginated animals, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getDiscoverPaginatedAnimalsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get discover paginated animals with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getDiscoverPaginatedAnimalsUseCase().first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(paginatedAnimals))

        }
    }
}