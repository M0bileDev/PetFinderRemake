package com.example.petfinderremake.features.discover.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petfinderremake.common.ext.observeError
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.adapter.AnimalGridAdapter
import com.example.petfinderremake.common.presentation.adapter.AnimalTypeGridAdapter
import com.example.petfinderremake.common.presentation.navigation.AnimalDetailsNavigation
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsReceiver
import com.example.petfinderremake.common.presentation.screen.gallery.GalleryReceiver
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.features.discover.R
import com.example.petfinderremake.features.discover.databinding.FragmentDiscoverBinding
import com.example.petfinderremake.features.discover.presentation.navigation.DiscoverNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment : Fragment(), GalleryReceiver, AnimalDetailsReceiver {

    @Inject
    lateinit var discoverNavigation: DiscoverNavigation

    @Inject
    lateinit var galleryNavigation: GalleryNavigation

    @Inject
    lateinit var animalDetailsNavigation: AnimalDetailsNavigation

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiscoverViewModel by activityViewModels()
    private lateinit var animalGridAdapter: AnimalGridAdapter
    private lateinit var animalTypeGridAdapter: AnimalTypeGridAdapter

    private var observeEventJob: Job? = null
    private var observeUiStateJob: Job? = null
    private var observeGalleryReceiverJob: Job? = null
    private var observeAnimalDetailsReceiverJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeError(viewModel.storageError, requireView())
        observeError(viewModel.networkError, requireView())
        setupSwipeRefresh()
        setupAnimalRecyclerView()
        setupAnimalTypeRecyclerView()
        setupBottomDiscover()
        observeDiscoverEvent()
        observeUiState()
        setupGallery()
        setupAnimalDetails()
    }

    private fun setupAnimalDetails() {
        observeAnimalDetailsReceiverJob?.cancel()
        observeAnimalDetailsReceiverJob = setupAnimalDetailsReceiver(
            { viewModel },
            { animalDetailsNavigation.navigateToAnimalDetails(this, it) }
        )
    }

    private fun setupGallery() {
        observeGalleryReceiverJob?.cancel()
        observeGalleryReceiverJob = setupGalleryReceiver(
            { viewModel },
            { galleryNavigation.navigateToGallery(this, it) })
    }

    private fun setupSwipeRefresh() =
        with(binding.discoverSwipeRefresh) {
            setOnRefreshListener {
                viewModel.requestData()
            }
        }

    private fun setupAnimalRecyclerView() = with(viewModel) {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        animalGridAdapter = AnimalGridAdapter(
            resources = requireContext().resources,
            onViewDetailsClick = { id ->
                navigateToAnimalDetails(id)
            },
            onImageClick = { id ->
                navigateToGallery(GallerySender.GalleryArg.GalleryId(id))
            }
        )
        binding.discoverTop.discoverTopRecyclerView.apply {
            adapter = animalGridAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun setupAnimalTypeRecyclerView() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        animalTypeGridAdapter = AnimalTypeGridAdapter(
            onClick = { name ->
                viewModel.navigateToSearch(name)
            }
        )
        binding.discoverBody.discoverBodyRecyclerView.apply {
            adapter = animalTypeGridAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun observeDiscoverEvent() {
        observeEventJob = withLifecycleOwner {
            viewModel.discoverEvent.collectLatest { event ->
                when (event) {
                    is DiscoverViewModel.DiscoverEvent.NavigateToSearch -> {
                        discoverNavigation.navigateToSearch(
                            this@DiscoverFragment,
                            event.typeName
                        )
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        observeUiStateJob = withLifecycleOwner {
            viewModel.discoverUiState.collectLatest { uiState ->
                updateSwipeRefresh(uiState)
                updateTopDiscover(uiState)
                updateBodyDiscover(uiState)
            }
        }
    }


    private fun updateSwipeRefresh(uiState: DiscoverUiState) = with(uiState) {
        binding.discoverSwipeRefresh.apply {
            if (isRefreshing && allLoadingsFinished) {
                isRefreshing = false
            }
        }
    }

    private fun updateTopDiscover(uiState: DiscoverUiState) = with(uiState) {
        binding.discoverTopLoading.root.isVisible = isTopLoading
        binding.discoverTop.root.isVisible = !isTopLoading

        if (isTopLoading) return@with
        binding.discoverTop.discoverTopHeadline.text =
            resources.getString(R.string.there_are_x_animals_ready_for_adoption, totalCount)
        animalGridAdapter.submitList(adapterModels)
    }

    private fun updateBodyDiscover(
        uiState: DiscoverUiState
    ) = with(uiState) {
        binding.discoverBodyLoading.root.isVisible = isBottomLoading
        binding.discoverBody.root.isVisible = !isBottomLoading

        if (isBottomLoading) return@with
        animalTypeGridAdapter.submitList(animalTypes)
    }

    private fun setupBottomDiscover() {
        binding.discoverBottom.discoverSeeMoreButton.setOnClickListener { _ ->
            viewModel.navigateToSearch()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        observeEventJob?.cancel()
        observeUiStateJob?.cancel()
        observeGalleryReceiverJob?.cancel()
        binding.discoverTop.discoverTopRecyclerView.adapter = null
        binding.discoverBody.discoverBodyRecyclerView.adapter = null
        _binding = null
    }
}