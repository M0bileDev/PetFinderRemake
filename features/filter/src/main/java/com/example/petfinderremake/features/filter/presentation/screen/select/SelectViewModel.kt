package com.example.petfinderremake.features.filter.presentation.screen.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.isEmpty
import com.example.petfinderremake.common.ext.checkIfItemTheSame
import com.example.petfinderremake.common.ext.checkIfItemsTheSame
import com.example.petfinderremake.features.filter.presentation.model.adapter.toSelectAdapterModel
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor() : ViewModel() {

    sealed interface UpdateFilterModel {
        data class Select(val name: String) : UpdateFilterModel
        data object Clear : UpdateFilterModel
        data object Idle : UpdateFilterModel
    }

    private var updateFilterJob: Job? = null
    private var doneJob: Job? = null
    private var clearJob: Job? = null

    sealed interface SelectEvent {
        data class NavigateBackWithResult(val resultNavArg: AnimalParameters) : SelectEvent
    }

    private var _selectEvent = Channel<SelectEvent>()
    val selectEvent get() = _selectEvent.receiveAsFlow()

    private var selectType = SelectType.TYPE
    private var filter = MutableStateFlow(AnimalParameters.noAnimalParameters)
    private val searchPhrase = MutableStateFlow("")
    private val names: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    private val filteredNames = combine(names, filter) { names, filter ->
        val filteredNames = with(filter) {
            if (isEmpty()) {
                names.map { it to false }
            } else {
                when (selectType) {
                    SelectType.TYPE -> {
                        type?.let { type ->
                            names.map { it to it.checkIfItemTheSame(type) }
                        } ?: names.map { it to false }
                    }

                    SelectType.BREED -> {
                        breed?.let { breed ->
                            if (breed.isEmpty()) {
                                names.map { it to false }
                            } else {
                                names.map { it to breed.checkIfItemsTheSame(it) }
                            }
                        } ?: names.map { it to false }
                    }
                }
            }
        }
        filteredNames
    }

    val selectUiState =
        combine(searchPhrase, filteredNames, filter) { searchPhrase, filteredNames, filter ->

            val search = searchPhrase.lowercase().trim()
            val searchedNames = getSearchedNames(search, filteredNames)
            val selectAdapterModel = searchedNames.toSelectAdapterModel()
            val isSelected = when (selectType) {
                SelectType.TYPE -> filter.type?.isNotEmpty()
                SelectType.BREED -> filter.breed?.isNotEmpty()
            } ?: false

            SelectUiState(
                selectAdapterModel = selectAdapterModel,
                selectType = selectType,
                isSelected = isSelected
            )
        }

    private fun getSearchedNames(
        searchPhraseFiltered: String,
        filteredNames: List<Pair<String, Boolean>>
    ) = if (searchPhraseFiltered.isEmpty()) {
        filteredNames
    } else {
        filteredNames.filter {
            it.first.lowercase().contains(searchPhraseFiltered)
        }
    }

    fun onSearch(value: String) {
        searchPhrase.value = value
    }

    fun onSelect(selectedName: String) {
        updateFilter(UpdateFilterModel.Select(selectedName))
    }

    private fun updateFilter(updateFilterModel: UpdateFilterModel = UpdateFilterModel.Idle) =
        with(updateFilterModel) {
            when (this) {
                is UpdateFilterModel.Clear -> {
                    clearFilter()
                }

                is UpdateFilterModel.Select -> {
                    selectFilter(name)
                }

                is UpdateFilterModel.Idle -> return
            }
        }

    private fun selectFilter(name: String) {
        updateFilterJob?.cancel()
        updateFilterJob = viewModelScope.launch {
            val names = names.value
            val filteredPairNameChecked = filteredNames.first()

            val selectedNames = names.map {
                it to selectValueLogic(
                    selectType.isSingleSelect, it, name, filteredPairNameChecked
                )
            }


            val updatedFilter = when (selectType) {
                SelectType.TYPE -> {
                    val type = selectedNames.find { it.second }?.first
                    filter.value.copy(type = type)
                }

                SelectType.BREED -> {
                    val breeds = selectedNames.filter { it.second }.map { it.first }
                    val filteredBreeds = breeds.ifEmpty { null }
                    filter.value.copy(breed = filteredBreeds)
                }
            }
            filter.value = updatedFilter
        }
    }

    private fun clearFilter() {
        val updatedFilter = when (selectType) {
            SelectType.TYPE -> {
                filter.value.copy(type = "")
            }

            SelectType.BREED -> {
                filter.value.copy(breed = emptyList())
            }
        }

        filter.value = updatedFilter
    }

    /**
     * Single select: only one value can be selected
     * Multi-select: More than one value can be selected
     */
    private fun selectValueLogic(
        singleSelect: Boolean,
        value: String,
        selectedValue: String,
        selectedValues: List<Pair<String, Boolean>>
    ): Boolean {

        //Condition for other list values (rest of the list)
        if (value != selectedValue) return if (singleSelect) false else selectedValues.first { it.first == value }.second

        //if value not exists in list then select else unselect
        val valueExists = selectedValues.first { it.first == selectedValue }
        return !valueExists.second

    }


    fun onDone() {
        doneJob?.cancel()
        doneJob = viewModelScope.launch {
            val filter = filter.value
            _selectEvent.send(SelectEvent.NavigateBackWithResult(filter))
        }
    }

    fun onClear() {
        clearJob?.cancel()
        clearJob = viewModelScope.launch {
            updateFilter(UpdateFilterModel.Clear)
        }
    }

    fun setupNavArgs(navArgs: SelectNavArg) {
        selectType = navArgs.selectType
        names.value = navArgs.names
        filter.value = navArgs.filter
    }

    override fun onCleared() {
        clearJob?.cancel()
        doneJob?.cancel()
        updateFilterJob?.cancel()
    }
}