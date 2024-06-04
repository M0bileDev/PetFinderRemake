package com.example.petfinderremake.features.details.animals.domain

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.features.details.animals.AnimalRepositoryTest
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetAnimalUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalUseCase: GetAnimalUseCase
    private val animal = AnimalWithDetails.noAnimalWithDetails
    private val animals = listOf(animal)

    @Before
    fun setup() {
        getAnimalUseCase = GetAnimalUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimals(animals)
        }
    }

    @Test
    fun `when get animal, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getAnimalUseCase(animal.id).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get animal, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getAnimalUseCase(animal.id).first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get animal with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getAnimalUseCase(animal.id).first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(animal))

        }
    }
}