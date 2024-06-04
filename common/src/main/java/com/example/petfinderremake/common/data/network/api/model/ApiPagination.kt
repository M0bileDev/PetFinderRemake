package com.example.petfinderremake.common.data.network.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPagination(
    @Json(name = "count_per_page") val countPerPage: Int?,
    @Json(name = "total_count") val totalCount: Int?,
    @Json(name = "current_page") val currentPage: Int?,
    @Json(name = "total_pages") val totalPages: Int?
)