package com.example.petfinderremake.features.filter.presentation.screen.select

import com.example.petfinderremake.features.filter.presentation.model.adapter.SelectAdapterModel
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType

data class SelectUiState(
    val selectAdapterModel: List<SelectAdapterModel> = emptyList(),
    val selectType: SelectType = SelectType.TYPE,
    val isSelected: Boolean = false
)