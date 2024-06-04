package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAddress(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?
)