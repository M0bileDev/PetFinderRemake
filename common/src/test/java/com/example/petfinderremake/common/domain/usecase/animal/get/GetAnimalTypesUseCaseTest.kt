package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetAnimalTypesUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalTypesUseCase: GetAnimalTypesUseCase
    private val types = listOf(AnimalType(emptyList(), emptyList(), emptyList(), "Type1"))

    @Before
    fun setup() {
        getAnimalTypesUseCase = GetAnimalTypesUseCase(animalRepository)
        animalRepository.storeAnimalTypes(types)
    }

    @Test
    fun `when get types, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalType>, NotYetDefinedError>>()
        val result = getAnimalTypesUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get types, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalType>, NotYetDefinedError>>()
        val result = getAnimalTypesUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get types with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<List<AnimalType>, NotYetDefinedError>>()
        val result = getAnimalTypesUseCase()
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(types))

    }

}