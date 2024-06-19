package com.example.petfinderremake.common.domain.usecase.animal.get

import com.example.petfinderremake.AnimalRepositoryTest
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import com.example.petfinderremake.common.domain.result.NotYetDefinedError
import com.example.petfinderremake.common.domain.result.Result
import com.example.petfinderremake.common.domain.result.RootError
import com.example.petfinderremake.common.domain.result.error.ArgumentError
import com.example.petfinderremake.common.domain.result.onError
import com.google.common.truth.Truth
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class GetAnimalTypeUseCaseTest : AnimalRepositoryTest() {

    private lateinit var getAnimalTypeUseCase: GetAnimalTypeUseCase
    private val type = "Type1"
    private val emptyType = ""
    private val animalType = AnimalType(emptyList(), emptyList(), emptyList(), type)
    private val types = listOf(animalType)

    @Before
    fun setup() {
        getAnimalTypeUseCase = GetAnimalTypeUseCase(animalRepository)
        animalRepository.storeAnimalTypes(types)
    }

    @Test
    fun `when get type, then result of use case is instance of Result`() {
        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result::class.java)
    }

    @Test
    fun `when get type, then result of use case is instance of Result Success`() {
        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when get type with previously set value, then result of type Result Success is the same value`() {
        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(type)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isEqualTo(Result.Success(animalType))
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is  instance of Result Error `() {
        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(emptyType)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is instance of Error ArgumentError`() {
        var error: RootError? = null

        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(emptyType)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        sut.onError { error = it.error }
        Truth.assertThat(error).isInstanceOf(ArgumentError::class.java)
    }

    @Test
    fun `when get empty type with previously set value, then result of use case is Error ArgumentError ARGUMENT_IS_EMPTY`() {
        //when
        val testObserver = TestObserver<Result<AnimalType, NotYetDefinedError>>()
        val result = getAnimalTypeUseCase(emptyType)
        result.subscribe(testObserver)

        //then
        val sut = testObserver.values().first()
        Truth.assertThat(sut)
            .isEqualTo(Result.Error(ArgumentError.ARGUMENT_IS_EMPTY))
    }
}