package com.example.petfinderremake.features.gallery.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.features.gallery.databinding.FragmentGalleryBinding
import com.example.petfinderremake.features.gallery.presentation.adapter.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryAdapter: GalleryAdapter

    private var observeUiStateJob: Job? = null
    private var observeEventJob: Job? = null

    @Inject
    lateinit var commonNavigation: CommonNavigation

    @Inject
    lateinit var galleryNavigation: GalleryNavigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupArgs()
        setupRecyclerView()
        setupCloseButton()
        observeUiState()
        observeGalleryEvent()
    }

    private fun setupCloseButton() = with(binding) {
        galleryCloseButton.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun observeGalleryEvent() = with(viewModel) {
        observeEventJob = withLifecycleOwner {
            galleryEvent.collectLatest { event ->
                when (event) {
                    GalleryViewModel.GalleryEvent.NavigateBack -> {
                        commonNavigation.navigateBack(this@GalleryFragment)
                    }
                }
            }
        }
    }

    private fun setupArgs() = with(viewModel) {
        val args = galleryNavigation.getGalleryNavArg(this@GalleryFragment)
        setupArgs(args)
    }

    private fun setupRecyclerView() = with(binding) {
        galleryAdapter = GalleryAdapter()
        galleryViewPager.apply {
            adapter = galleryAdapter
        }
    }

    private fun observeUiState() = with(viewModel) {
        observeUiStateJob = withLifecycleOwner {
            galleryUiState.collectLatest { uiState ->
                updateRecyclerView(uiState)
            }
        }
    }

    private fun updateRecyclerView(uiState: GalleryUiState) = with(uiState) {
        galleryAdapter.submitList(galleryAdapterModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeUiStateJob?.cancel()
        observeEventJob?.cancel()
    }
}