package com.example.petfinderremake.features.details.animals.domain

import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.features.details.animals.AnimalRepositoryTest
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test


class GetAnimalUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalUseCase: GetAnimalUseCase
    private val animal = AnimalWithDetails.noAnimalWithDetails
    private val animals = listOf(animal)

    @Before
    fun setup() {
        getAnimalUseCase = GetAnimalUseCase(animalRepository)
        animalRepository.storeAnimals(animals)
    }

    @Test
    fun `when get animal, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<AnimalWithDetails, NotYetDefinedError>>()
        val result = getAnimalUseCase(animal.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get animal, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<AnimalWithDetails, NotYetDefinedError>>()
        val result = getAnimalUseCase(animal.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get animal with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<AnimalWithDetails, NotYetDefinedError>>()
        val result = getAnimalUseCase(animal.id)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(animal))
    }
}