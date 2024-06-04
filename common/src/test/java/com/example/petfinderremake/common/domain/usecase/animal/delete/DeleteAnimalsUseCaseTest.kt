package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteAnimalsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var deleteAnimalsUseCase: DeleteAnimalsUseCase
    private val animalsList =
        listOf(AnimalWithDetails.noAnimalWithDetails, AnimalWithDetails.noAnimalWithDetails)

    @Before
    fun setup() {
        deleteAnimalsUseCase = DeleteAnimalsUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimals(animalsList)
        }
    }

    @Test
    fun `when delete animals, then stored animals list is empty`() {
        runBlocking {

            //when
            deleteAnimalsUseCase()

            //then
            val storedAnimals = animalRepository.getAnimals().first()
            Truth.assertThat(storedAnimals).isEmpty()
        }
    }

    @Test
    fun `when delete animals, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = deleteAnimalsUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when delete animals, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = deleteAnimalsUseCase()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when delete animals, then result of type Result Success is Unit`() {
        runBlocking {

            //when
            val result = deleteAnimalsUseCase()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(Unit))

        }
    }


}