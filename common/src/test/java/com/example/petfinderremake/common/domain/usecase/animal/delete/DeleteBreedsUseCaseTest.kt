package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DeleteBreedsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var deleteBreedsUseCase: DeleteBreedsUseCase
    private val breeds =
        listOf(AnimalBreeds("Breed1"), AnimalBreeds("Breed2"))

    @Before
    fun setup() {
        deleteBreedsUseCase = DeleteBreedsUseCase(animalRepository)
        animalRepository.storeAnimalBreeds(breeds)
    }

    @Test
    fun `when delete breeds, then stored breeds list is empty`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteBreedsUseCase().subscribe(testObserver)

        //then
        val storedBreeds = animalRepository.getAnimalBreeds()
        val testObserver2 = TestObserver<List<AnimalBreeds>>()
        storedBreeds.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEmpty()
    }

    @Test
    fun `when delete breeds, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when delete breeds, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when delete breeds, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteBreedsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }
}