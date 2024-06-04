package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPhotoSizes(
    val small: String?,
    val medium: String?,
    val large: String?,
    val full: String?
)