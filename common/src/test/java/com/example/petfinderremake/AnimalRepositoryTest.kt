package com.example.petfinderremake

import com.example.petfinderremake.common.data.repositories.FakeAnimalRepository
import org.junit.Rule

abstract class AnimalRepositoryTest {

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    val animalRepository = FakeAnimalRepository()
}