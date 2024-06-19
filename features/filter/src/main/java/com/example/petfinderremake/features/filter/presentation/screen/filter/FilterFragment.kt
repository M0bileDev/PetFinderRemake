package com.example.petfinderremake.features.filter.presentation.screen.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.petfinderremake.common.domain.model.isNotEmpty
import com.example.petfinderremake.common.ext.subscribeError
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.features.filter.databinding.FragmentFilterBinding
import com.example.petfinderremake.features.filter.presentation.navigation.FilterNavigation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private val viewModel: FilterViewModel by activityViewModels()
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private var observeResultJob: Job? = null
    private var observeEventJob: Job? = null
    private var observeUiStateJob: Job? = null

    private var networkErrorJob: Job? = null
    private var storageErrorJob: Job? = null

    private val subscriptions = CompositeDisposable()

    @Inject
    lateinit var filterNavigation: FilterNavigation

    @Inject
    lateinit var commonNavigation: CommonNavigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeError(
            requireView(),
            viewModel.storageError,
            { storageErrorJob = it },
            { it.addTo(subscriptions) }
        )
        subscribeError(
            requireView(),
            viewModel.networkError,
            { networkErrorJob = it },
            { it.addTo(subscriptions) }
        )
        setupNavArgs()
        observeNavigationResult()
        setupFilterTypeGroup()
        setupFilterBreedGroup()
        setupBottomButtons()
        setupRefresh()
        setupToolbar()
        observeUiState()
        observeFilterEvent()
    }

    private fun setupToolbar() = with(binding) {
        filterTopAppBar.setNavigationOnClickListener {
            commonNavigation.navigateBack(this@FilterFragment)
        }
    }

    private fun setupRefresh() = with(binding) {
        filterSwipeRefresh.setOnRefreshListener {
            viewModel.onRefresh()
        }
    }

    private fun setupNavArgs() = with(filterNavigation) {
        val filterNavArg = getFilterNavArg(this@FilterFragment)
        if (filterNavArg.isNotEmpty()) {
            viewModel.setupFilterNavArg(filterNavArg)
            clearFilterNavArgs(this@FilterFragment)
        }
    }

    private fun observeNavigationResult() = with(filterNavigation) {
        withLifecycleOwner(
            jobBlock = { observeResultJob = it },
            disposableBlock = {
                observeResultNavArg(this@FilterFragment)
                    .subscribe { resultNavArg ->
                        viewModel.setupResultNavArg(resultNavArg)
                        clearResultNavArg(this@FilterFragment)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun observeFilterEvent() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeEventJob = it
            },
            disposableBlock = {
                filterEvent
                    .subscribe { event ->
                        when (event) {
                            is FilterViewModel.FilterEvent.NavigateBackWithResult -> {
                                filterNavigation.navigateBackWithResult(
                                    this@FilterFragment,
                                    event.resultNavArg
                                )
                            }

                            is FilterViewModel.FilterEvent.NavigateToSelect -> {
                                filterNavigation.navigateToSelect(
                                    this@FilterFragment,
                                    event.selectNavArg
                                )
                            }
                        }
                    }.addTo(subscriptions)
            }
        )
    }

    private fun setupBottomButtons() = with(binding) {
        filterButtonApply.setOnClickListener {
            viewModel.onApplyFilter()
        }
        filterButtonClear.setOnClickListener {
            viewModel.onClearFilter()
        }
    }


    private fun setupFilterTypeGroup() = with(binding) {
        filterTypeButton.setOnClickListener {
            viewModel.onSelectAnimalType()
        }
    }

    private fun setupFilterBreedGroup() = with(binding) {
        filterBreedButton.setOnClickListener {
            viewModel.onSelectAnimalBreed()
        }
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeUiStateJob = it
            },
            disposableBlock = {
                filterUiState
                    .subscribe { uiState ->
                        updateRefresh(uiState)
                        updateFilterTypeGroup(uiState)
                        updateFilterBreedsGroup(uiState)
                        updateBottom(uiState)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateRefresh(uiState: FilterUiState) = with(uiState) {
        binding.filterSwipeRefresh.isRefreshing = isLoading
    }

    private fun updateBottom(uiState: FilterUiState) = with(uiState) {
        binding.filterButtonClear.isEnabled = isSelected
    }

    private fun updateFilterTypeGroup(uiState: FilterUiState) = with(uiState) {
        binding.filterTypeButton.isEnabled = isFilerTypeButtonEnable
    }


    private fun updateFilterBreedsGroup(uiState: FilterUiState) = with(uiState) {
        binding.filterBreedButton.isEnabled = isFilterBreedButtonEnabled
    }


    override fun onDestroyView() {
        super.onDestroyView()
        observeResultJob?.cancel()
        observeEventJob?.cancel()
        observeUiStateJob?.cancel()
        subscriptions.clear()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

}