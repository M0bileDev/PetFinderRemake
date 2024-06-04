package com.example.petfinderremake.common.data.network.mapper

import com.example.petfinderremake.common.data.network.api.model.ApiBreed
import com.example.petfinderremake.common.domain.model.animal.AnimalBreeds
import javax.inject.Inject

class ApiAnimalBreedsMapper @Inject constructor() : ApiMapper<ApiBreed, AnimalBreeds> {

    override fun mapToDomain(apiEntity: ApiBreed): AnimalBreeds {
        return AnimalBreeds(name = apiEntity.name.orEmpty())
    }

}