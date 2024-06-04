package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAttributes(
    @Json(name = "spayed_neutered") val spayedNeutered: Boolean?,
    @Json(name = "house_trained") val houseTrained: Boolean?,
    val declawed: Boolean?,
    @Json(name = "special_needs") val specialNeeds: Boolean?,
    @Json(name = "shots_current") val shotsCurrent: Boolean?
)