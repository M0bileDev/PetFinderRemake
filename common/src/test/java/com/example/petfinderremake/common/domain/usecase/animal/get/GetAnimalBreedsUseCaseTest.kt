package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetAnimalBreedsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalBreedsUseCase: GetAnimalBreedsUseCase
    private val breeds =
        listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"))

    @Before
    fun setup() {
        getAnimalBreedsUseCase = GetAnimalBreedsUseCase(animalRepository)
        animalRepository.storeAnimalBreeds(breeds)
    }

    @Test
    fun `when get breeds, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalBreeds>, NotYetDefinedError>>()
        val result =  getAnimalBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get breeds, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalBreeds>, NotYetDefinedError>>()
        val result =  getAnimalBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get breeds with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalBreeds>, NotYetDefinedError>>()
        val result =  getAnimalBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(breeds))
    }
}