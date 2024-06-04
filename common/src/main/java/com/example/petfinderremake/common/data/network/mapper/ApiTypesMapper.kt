package com.example.petfinderremake.common.data.network.mapper

import com.example.petfinderremake.common.data.network.api.model.ApiType
import com.example.petfinderremake.common.domain.model.animal.AnimalType
import javax.inject.Inject

class ApiTypesMapper @Inject constructor() : ApiMapper<ApiType, AnimalType> {

    override fun mapToDomain(apiEntity: ApiType): AnimalType {
        return AnimalType(
            coats = apiEntity.coats.orEmpty(),
            colors = apiEntity.colors.orEmpty(),
            genders = apiEntity.genders.orEmpty(),
            name = apiEntity.name.orEmpty()
        )
    }
}