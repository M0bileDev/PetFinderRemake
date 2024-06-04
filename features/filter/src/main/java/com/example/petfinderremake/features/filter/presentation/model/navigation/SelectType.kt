package com.example.petfinderremake.features.filter.presentation.model.navigation

import com.example.petfinderremake.common.presentation.utils.commonString

enum class SelectType(val isSingleSelect: Boolean) {
    TYPE(true),
    BREED(false)
}

fun SelectType.toStringResource(): Int {
    return when (this) {
        SelectType.TYPE -> commonString.type
        SelectType.BREED -> commonString.breed
    }
}