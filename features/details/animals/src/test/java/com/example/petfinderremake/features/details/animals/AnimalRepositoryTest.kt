package com.example.petfinderremake.features.details.animals

import org.junit.Rule

abstract class AnimalRepositoryTest {

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    val animalRepository = FakeAnimalRepository()
}