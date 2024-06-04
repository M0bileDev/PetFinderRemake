package com.example.petfinderremake.common.domain.model.search

import com.example.petfinderremake.common.domain.model.animal.Animal
import com.example.petfinderremake.common.domain.model.search.SearchParameters

data class SearchResults(
    val animals: List<Animal>,
    val searchParameters: SearchParameters
)
