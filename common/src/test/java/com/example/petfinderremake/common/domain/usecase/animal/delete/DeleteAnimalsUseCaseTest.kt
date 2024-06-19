package com.example.petfinderremake.common.domain.usecase.animal.delete

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.details.AnimalWithDetails
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class DeleteAnimalsUseCaseTest : AnimalRepositoryTest() {

    private lateinit var deleteAnimalsUseCase: DeleteAnimalsUseCase
    private val animalsList =
        listOf(AnimalWithDetails.noAnimalWithDetails, AnimalWithDetails.noAnimalWithDetails)

    @Before
    fun setup() {
        deleteAnimalsUseCase = DeleteAnimalsUseCase(animalRepository)
        animalRepository.storeAnimals(animalsList)
    }

    @Test
    fun `when delete animals, then stored animals list is empty`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        deleteAnimalsUseCase().subscribe(testObserver)

        //then
        val testObserver2 = TestObserver<List<AnimalWithDetails>>()
        val storedAnimals = animalRepository.getAnimals()
        storedAnimals.subscribe(testObserver2)

        val sut = testObserver2.values().first()
        Truth.assertThat(sut).isEmpty()
    }

    @Test
    fun `when delete animals, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when delete animals, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when delete animals, then result of type Result Success is Unit`() {
        //when
        val testObserver = TestObserver<Result<Unit, NotYetDefinedError>>()
        val result = deleteAnimalsUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(Unit))
    }


}