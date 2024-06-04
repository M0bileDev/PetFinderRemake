package com.example.petfinderremake.common.domain.model.animal

data class AnimalType(
    val coats: List<String>,
    val colors: List<String>,
    val genders: List<String>,
    val name: String
)