package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAnimalTypesUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalTypesUseCase: GetAnimalTypesUseCase
    private val types = listOf(AnimalType(emptyList(), emptyList(), emptyList(), "Type1"))

    @Before
    fun setup() {
        getAnimalTypesUseCase = GetAnimalTypesUseCase(animalRepository)

        runBlocking {
            animalRepository.storeAnimalTypes(types)
        }
    }

    @Test
    fun `when get types, then result of use case is instance of Result`() {
        runBlocking {

            //when
            val result = getAnimalTypesUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result::class.java)

        }
    }

    @Test
    fun `when get types, then result of use case is instance of Result Success`() {
        runBlocking {

            //when
            val result = getAnimalTypesUseCase().first()

            //then
            Truth.assertThat(result).isInstanceOf(Result.Success::class.java)

        }
    }

    @Test
    fun `when get types with previously set value, then result of type Result Success is the same value`() {
        runBlocking {

            //when
            val result = getAnimalTypesUseCase().first()

            //then
            Truth.assertThat(result).isEqualTo(Result.Success(types))

        }
    }

}