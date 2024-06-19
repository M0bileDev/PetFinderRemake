package com.example.petfinderremake.features.filter.presentation.screen.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.common.presentation.utils.commonString
import com.example.petfinderremake.features.filter.databinding.FragmentSelectBinding
import com.example.petfinderremake.features.filter.presentation.adapter.SelectAdapter
import com.example.petfinderremake.features.filter.presentation.model.navigation.toStringResource
import com.example.petfinderremake.features.filter.presentation.navigation.SelectNavigation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class SelectFragment : Fragment() {

    private val viewModel: SelectViewModel by viewModels()
    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectAdapter: SelectAdapter

    private var observeEventJob: Job? = null
    private var observeUiStateJob: Job? = null
    private val subscriptions = CompositeDisposable()

    @Inject
    lateinit var selectNavigation: SelectNavigation

    @Inject
    lateinit var commonNavigation: CommonNavigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupArgs()
        setupRecyclerView()
        setupSearchPhrase()
        setupBottomButtons()
        setupToolbar()
        observeUiState()
        observeSelectEvent()
    }

    private fun setupToolbar() = with(binding) {
        selectTopAppBar.setNavigationOnClickListener {
            commonNavigation.navigateBack(this@SelectFragment)
        }
    }

    private fun setupArgs() = with(viewModel) {
        val args = selectNavigation.getSelectNavArg(this@SelectFragment)
        setupNavArgs(args)
    }

    private fun setupRecyclerView() = with(binding) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        selectAdapter = SelectAdapter(onClick = { name ->
            viewModel.onSelect(name)
        })
        selectRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = selectAdapter
        }
    }

    private fun setupSearchPhrase() = with(binding) {
        selectEditText.addTextChangedListener { text ->
            viewModel.onSearch(text.toString())
        }
    }

    private fun setupBottomButtons() = with(binding) {
        selectButtonReady.setOnClickListener {
            viewModel.onDone()
        }
        selectButtonClear.setOnClickListener {
            viewModel.onClear()
        }
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeUiStateJob = it
            },
            disposableBlock = {
                selectUiState
                    .subscribe { uiState ->
                        updateRecyclerView(uiState)
                        updateClearButton(uiState)
                        updateToolbar(uiState)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateToolbar(uiState: SelectUiState) = with(uiState) {
        val selectTypeString = getString(selectType.toStringResource())
        binding.selectTopAppBar.title =
            getString(commonString.select_x, selectTypeString)
    }

    private fun updateRecyclerView(uiState: SelectUiState) = with(uiState) {
        selectAdapter.submitList(selectAdapterModel)
    }

    private fun updateClearButton(uiState: SelectUiState) = with(uiState) {
        binding.selectButtonClear.isEnabled = isSelected
    }

    private fun observeSelectEvent() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeEventJob = it
            },
            disposableBlock = {
                selectEvent
                    .subscribe { event ->
                        when (event) {
                            is SelectViewModel.SelectEvent.NavigateBackWithResult -> {
                                selectNavigation.navigateBackWithResult(
                                    this@SelectFragment,
                                    event.resultNavArg
                                )
                            }
                        }
                    }.addTo(subscriptions)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeEventJob?.cancel()
        observeUiStateJob?.cancel()
        subscriptions.clear()
        binding.selectRecyclerView.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}