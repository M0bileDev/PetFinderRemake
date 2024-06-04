package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedAnimals(
    val animals: List<ApiAnimal>?,
    val pagination: ApiPagination?
)