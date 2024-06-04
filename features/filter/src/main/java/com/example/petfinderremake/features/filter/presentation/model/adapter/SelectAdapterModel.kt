package com.example.petfinderremake.features.filter.presentation.model.adapter

data class SelectAdapterModel(
    val id: Int,
    val name: String,
    val checked: Boolean
)

fun List<Pair<String, Boolean>>.toSelectAdapterModel() =
    mapIndexed { index, pair ->
        SelectAdapterModel(index, pair.first, pair.second)
    }
