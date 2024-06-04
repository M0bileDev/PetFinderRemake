package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiContact(
    val email: String?,
    val phone: String?,
    val address: ApiAddress?
)