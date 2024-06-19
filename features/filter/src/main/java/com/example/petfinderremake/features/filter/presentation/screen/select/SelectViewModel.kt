package com.example.petfinderremake.features.filter.presentation.screen.select

import androidx.lifecycle.ViewModel
import com.example.petfinderremake.common.domain.model.AnimalParameters
import com.example.petfinderremake.common.domain.model.isEmpty
import com.example.petfinderremake.common.ext.checkIfItemTheSame
import com.example.petfinderremake.common.ext.checkIfItemsTheSame
import com.example.petfinderremake.common.ext.getValueOrThrow
import com.example.petfinderremake.features.filter.presentation.model.adapter.toSelectAdapterModel
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectNavArg
import com.example.petfinderremake.features.filter.presentation.model.navigation.SelectType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor() : ViewModel() {

    sealed interface UpdateFilterModel {
        data class Select(val name: String) : UpdateFilterModel
        data object Clear : UpdateFilterModel
        data object Idle : UpdateFilterModel
    }

    sealed interface SelectEvent {
        data class NavigateBackWithResult(val resultNavArg: AnimalParameters) : SelectEvent
    }

    private val selectEventSubject = PublishSubject.create<SelectEvent>()
    val selectEvent = selectEventSubject
        .hide()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private var selectType = SelectType.TYPE
    private val filterSubject = BehaviorSubject.createDefault(AnimalParameters.noAnimalParameters)
    private val searchPhraseSubject = BehaviorSubject.createDefault("")
    private val namesSubject = BehaviorSubject.createDefault<List<String>>(emptyList())

    private val filteredNames =
        Observable.combineLatest(namesSubject, filterSubject) { names, filter ->
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
        Observable.combineLatest(
            searchPhraseSubject,
            filteredNames,
            filterSubject
        ) { searchPhrase, filteredNames, filter ->

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
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

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
        searchPhraseSubject.onNext(value)
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

        val names = namesSubject.getValueOrThrow()
        val filter = filterSubject.getValueOrThrow()
        // TODO: Test this solution
        val filteredPairNameChecked = filteredNames.blockingFirst()

        val selectedNames = names.map {
            it to selectValueLogic(
                selectType.isSingleSelect, it, name, filteredPairNameChecked
            )
        }


        val updatedFilter = when (selectType) {
            SelectType.TYPE -> {
                val type = selectedNames.find { it.second }?.first
                filter.copy(type = type)
            }

            SelectType.BREED -> {
                val breeds = selectedNames.filter { it.second }.map { it.first }
                val filteredBreeds = breeds.ifEmpty { null }
                filter.copy(breed = filteredBreeds)
            }
        }
        filterSubject.onNext(updatedFilter)
    }

    private fun clearFilter() {
        val filter = filterSubject.getValueOrThrow()

        val updatedFilter = when (selectType) {
            SelectType.TYPE -> {
                filter.copy(type = "")
            }

            SelectType.BREED -> {
                filter.copy(breed = emptyList())
            }
        }

        filterSubject.onNext(updatedFilter)
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
        val filter = filterSubject.getValueOrThrow()
        selectEventSubject.onNext(SelectEvent.NavigateBackWithResult(filter))
    }

    fun onClear() {
        updateFilter(UpdateFilterModel.Clear)
    }

    fun setupNavArgs(navArgs: SelectNavArg) {
        selectType = navArgs.selectType
        namesSubject.onNext(navArgs.names)
        filterSubject.onNext(navArgs.filter)
    }

}