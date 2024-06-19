package com.example.petfinderremake.features.discover

import org.junit.Rule

abstract class AnimalRepositoryTest {

    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    val animalRepository = FakeAnimalRepository()
}