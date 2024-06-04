package com.example.petfinderremake.features.filter.presentation.screen.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.petfinderremake.common.domain.model.isNotEmpty
import com.example.petfinderremake.common.ext.observeError
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.features.filter.databinding.FragmentFilterBinding
import com.example.petfinderremake.features.filter.presentation.navigation.FilterNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private val viewModel: FilterViewModel by activityViewModels()
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private var observeResultJob: Job? = null
    private var observeEventJob: Job? = null
    private var observeUiStateJob: Job? = null

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
        observeError(viewModel.storageError, requireView())
        observeError(viewModel.networkError, requireView())
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
        observeResultJob = withLifecycleOwner {
            observeResultNavArg(this@FilterFragment).collectLatest { resultNavArg ->
                viewModel.setupResultNavArg(resultNavArg)
                clearResultNavArg(this@FilterFragment)
            }
        }
    }

    private fun observeFilterEvent() {
        observeEventJob = withLifecycleOwner {
            viewModel.filterEvent.collectLatest { event ->
                when (event) {
                    is FilterViewModel.FilterEvent.NavigateBackWithResult -> {
                        filterNavigation.navigateBackWithResult(this, event.resultNavArg)
                    }

                    is FilterViewModel.FilterEvent.NavigateToSelect -> {
                        filterNavigation.navigateToSelect(this, event.selectNavArg)
                    }
                }
            }
        }
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

    private fun observeUiState() {
        observeUiStateJob = withLifecycleOwner {
            viewModel.filterUiState.collectLatest { uiState ->
                updateRefresh(uiState)
                updateFilterTypeGroup(uiState)
                updateFilterBreedsGroup(uiState)
                updateBottom(uiState)
            }
        }
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
        _binding = null
    }

}