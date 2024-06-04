package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteBreedsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var deleteBreedsUseCase: DeleteBreedsUseCase
    private val breeds =
        listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"))

    @Before
    fun setup() {
        deleteBreedsUseCase = DeleteBreedsUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimalBreeds(breeds)
        }
    }

    @Test
    fun `when delete breeds, then stored breeds list is empty`() {
        runBlocking {

            //when
            deleteBreedsUseCase()

            //then
            val storedBreeds = animalRepository.getAnimalBreeds().first()
            Truth.assertThat(storedBreeds).isEmpty()
        }
    }

    @Test
    fun `when delete breeds, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = deleteBreedsUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when delete breeds, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = deleteBreedsUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when delete breeds, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = deleteBreedsUseCase()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }
}