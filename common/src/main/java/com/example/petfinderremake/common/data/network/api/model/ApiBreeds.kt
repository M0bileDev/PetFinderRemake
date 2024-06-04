package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiBreeds(
    val primary: String?,
    val secondary: String?,
    val mixed: Boolean?,
    val unknown: Boolean?
)