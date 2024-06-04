package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAnimalBreedsUseCaseTest : AnimalRepositoryTest(){
    
    private lateinit var getAnimalBreedsUseCase: GetAnimalBreedsUseCase
    private val breeds =
        listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"))
    
    @Before
    fun setup(){
        getAnimalBreedsUseCase = GetAnimalBreedsUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimalBreeds(breeds)
        }
    }

    @Test
    fun `when get breeds, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getAnimalBreedsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get breeds, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getAnimalBreedsUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get breeds with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getAnimalBreedsUseCase().first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(breeds))

        }
    }
}