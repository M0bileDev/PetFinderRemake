package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiType(
    val coats: List<String>?,
    val colors: List<String>?,
    val genders: List<String>?,
    val name: String?
)