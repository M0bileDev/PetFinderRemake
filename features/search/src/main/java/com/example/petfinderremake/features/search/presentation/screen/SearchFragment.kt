package com.example.petfinderremake.features.search.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.petfinderremake.common.ext.subscribeError
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.adapter.AnimalGridAdapter
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterEnum
import com.example.petfinderremake.common.presentation.navigation.AnimalDetailsNavigation
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.common.presentation.screen.animalDetails.AnimalDetailsReceiver
import com.example.petfinderremake.common.presentation.screen.gallery.GalleryReceiver
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.features.search.R
import com.example.petfinderremake.features.search.databinding.FragmentSearchBinding
import com.example.petfinderremake.features.search.presentation.navigation.SearchNavigation
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.coroutines.Job
import javax.inject.Inject


@ExperimentalBadgeUtils
@AndroidEntryPoint
class SearchFragment : Fragment(), GalleryReceiver, AnimalDetailsReceiver {

    @Inject
    lateinit var searchNavigation: SearchNavigation

    @Inject
    lateinit var galleryNavigation: GalleryNavigation

    @Inject
    lateinit var animalDetailsNavigation: AnimalDetailsNavigation

    companion object {
        private const val ITEMS_PER_ROW = 2

        enum class ResultViewType {
            LIST,
            GRID
        }
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var animalGridAdapter: AnimalGridAdapter

    private var observeResultJob: Job? = null
    private var observeEventJob: Job? = null
    private var observeUiStateJob: Job? = null
    private var observeGalleryReceiverJob: Job? = null
    private var observeAnimalDetailsReceiverJob: Job? = null

    private var storageErrorJob: Job? = null
    private var networkErrorJob: Job? = null

    private val subscriptions = CompositeDisposable()

    private var searchResultViewType = ResultViewType.GRID
    private lateinit var badge: BadgeDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
        observeSearchEvent()
        observeUiState()
        setupGridRecyclerView()
        setupToolbarMenu()
        setupFilterMenuBadge()
        setupRefresh()
        setupGallery()
        setupAnimalDetails()
    }

    private fun setupAnimalDetails() {
        setupAnimalDetailsReceiver(
            { viewModel },
            { animalDetailsNavigation.navigateToAnimalDetails(this, it) },
            { observeAnimalDetailsReceiverJob = it },
            { it.addTo(subscriptions) }
        )
    }

    private fun setupGallery() {
        setupGalleryReceiver(
            { viewModel },
            { galleryNavigation.navigateToGallery(this, it) },
            { observeGalleryReceiverJob = it },
            { it.addTo(subscriptions) }
        )
    }

    private fun setupRefresh() = with(binding) {
        searchSwipeRefresh.setOnRefreshListener {
            viewModel.onRefresh()
        }
    }

    private fun setupNavArgs() = with(searchNavigation) {
        val filterNavArg = getFilterNavArg(this@SearchFragment)
        viewModel.setupFilterNavArg(filterNavArg)
        clearFilterNavArgs(this@SearchFragment)
    }

    private fun observeNavigationResult() = with(searchNavigation) {
        withLifecycleOwner(
            jobBlock = {
                observeResultJob = it
            },
            disposableBlock = {
                observeResultNavArg(this@SearchFragment)
                    .subscribe { resultNavArg ->
                        viewModel.setupResultNavArg(resultNavArg)
                        clearResultNavArg(this@SearchFragment)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun setupFilterMenuBadge() {
        badge = BadgeDrawable.create(requireContext())
        BadgeUtils.attachBadgeDrawable(badge, binding.searchToolbar, R.id.filter)
    }

    private fun observeSearchEvent() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeEventJob = it
            },
            disposableBlock = {
                searchEvent
                    .subscribe { event ->
                        when (event) {
                            is SearchViewModel.SearchEvent.NavigateToFilter -> {
                                searchNavigation.navigateToFilter(
                                    this@SearchFragment,
                                    filterNavArg = event.filterNavArg
                                )
                            }
                        }
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateFilterMenuBadge(uiState: SearchUiState) = with(uiState) {
        if (isFilterActive) {
            badge.backgroundColor =
                resources.getColor(androidx.appcompat.R.color.error_color_material_dark, null)
        } else {
            badge.backgroundColor =
                resources.getColor(
                    android.R.color.transparent,
                    null
                )
        }
    }


    private fun setupGridRecyclerView() = with(viewModel) {
        animalGridAdapter =
            AnimalGridAdapter(
                resources = requireContext().resources,
                onViewDetailsClick = { id ->
                    navigateToAnimalDetails(id)
                },
                onImageClick = { id ->
                    navigateToGallery(GallerySender.GalleryArg.GalleryId(id))
                },
                onLoadMoreClick = ::onLoadMore
            )
        val gridLayoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
            .apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (animalGridAdapter.getItemViewType(position)) {
                            AnimalAdapterEnum.ANIMAL_GRID_ITEM.ordinal, AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM.ordinal -> 1
                            AnimalAdapterEnum.LOAD_MORE.ordinal -> 2
                            else -> throw Exception()
                        }
                    }
                }
            }
        binding.searchRecyclerView.apply {
            adapter = animalGridAdapter
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun setupToolbarMenu() = with(binding.searchToolbar) {
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.filter -> viewModel.onNavigateToFilter()

                R.id.result_view_type -> {
                    val updatedIcon = when (searchResultViewType) {
                        ResultViewType.LIST -> {
                            searchResultViewType = ResultViewType.GRID
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_grid,
                                null
                            )
                        }

                        ResultViewType.GRID -> {
                            searchResultViewType = ResultViewType.LIST
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_list,
                                null
                            )
                        }
                    }
                    menu.findItem(R.id.result_view_type).apply {
                        icon = updatedIcon
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeUiStateJob = it
            },
            disposableBlock = {
                searchUiState
                    .subscribe { uiState ->
                        updateAnimalGridAdapter(uiState)
                        updateFilterMenuBadge(uiState)
                        updateRefresh(uiState)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateRefresh(uiState: SearchUiState) = with(uiState) {
        binding.searchSwipeRefresh.isEnabled = isFilterActive
        binding.searchSwipeRefresh.isRefreshing = isLoading
    }


    private fun updateAnimalGridAdapter(uiState: SearchUiState) {
        with(animalGridAdapter) {
            apply { submitList(uiState.adapterModels) }
            apply { isLoadMore(uiState.isLoadMore) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeResultJob?.cancel()
        observeEventJob?.cancel()
        observeUiStateJob?.cancel()
        observeGalleryReceiverJob?.cancel()
        observeAnimalDetailsReceiverJob?.cancel()
        storageErrorJob?.cancel()
        networkErrorJob?.cancel()
        subscriptions.clear()
        BadgeUtils.detachBadgeDrawable(badge, binding.searchToolbar, R.id.filter)
        binding.searchRecyclerView.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}