package com.example.petfinderremake.common.ext

fun List<String>.checkIfItemsTheSame(
    item: String
): Boolean {
    val value = find { it == item }
    return when {
        value == null -> false
        else -> true
    }
}

fun String.checkIfItemTheSame(
    item: String
): Boolean {
    return this == item
}