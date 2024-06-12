package com.example.petfinderremake.features.details.animals.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petfinderremake.common.ext.ScaleType
import com.example.petfinderremake.common.ext.onClickAnimateWithAction
import com.example.petfinderremake.common.ext.openWebPage
import com.example.petfinderremake.common.ext.setImage
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.AnimalDetailsNavigation
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.common.presentation.navigation.GalleryNavigation
import com.example.petfinderremake.common.presentation.screen.gallery.GalleryReceiver
import com.example.petfinderremake.common.presentation.screen.gallery.GallerySender
import com.example.petfinderremake.common.presentation.utils.fromPresentationDataListToString
import com.example.petfinderremake.common.presentation.utils.fromPresentationDataToString
import com.example.petfinderremake.features.details.animals.R
import com.example.petfinderremake.features.details.animals.databinding.FragmentAnimalDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class AnimalDetailsFragment : Fragment(), GalleryReceiver {

    @Inject
    lateinit var animalDetailsNavigation: AnimalDetailsNavigation

    @Inject
    lateinit var galleryNavigation: GalleryNavigation

    @Inject
    lateinit var commonNavigation: CommonNavigation

    private var _binding: FragmentAnimalDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnimalDetailsViewModel by viewModels()

    private var observeUiStateJob: Job? = null
    private var observeGalleryReceiverJob: Job? = null
    private val subscriptions = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupArgs()
        setupGallery()
        setupToolbar()
        observeUiState()
    }

    private fun setupOpenInPageButton(uiState: DetailsUiState) =
        with(binding.detailsOpenInPageButton) {
            if (uiState.url.isEmpty()) return@with
            isEnabled = true
            setOnClickListener {
                requireContext().openWebPage(uiState.url)
            }
        }

    private fun setupToolbar() = with(binding) {
        detailsTopAppBar.setNavigationOnClickListener {
            commonNavigation.navigateBack(this@AnimalDetailsFragment)
        }
    }

    private fun setupGallery() {
        setupGalleryReceiver(
            { viewModel },
            { galleryNavigation.navigateToGallery(this, it) },
            { observeGalleryReceiverJob = it },
            { it.addTo(subscriptions) }
        )
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeUiStateJob = it
            },
            disposableBlock = {
                detailsUiState
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { uiState ->
                        updateDetailsTop(uiState)
                        updateDescription(uiState.descriptionSection)
                        updateDetails(uiState.detailsSection)
                        updateHealthDetails(uiState.healthDetailsSection)
                        updateHabitatAdaptation(uiState.habitatAdaptationSection)
                        setupOpenInPageButton(uiState)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateHabitatAdaptation(sectionDataList: SectionDataList) =
        with(binding.detailsExpandableHabitatAdaptation) {
            expandableTitle.text =
                getString(sectionDataList.first.stringId)
            expandableDescription.text =
                sectionDataList.second.fromPresentationDataListToString(requireContext())
            expandableButton.onClickAnimateWithAction {
                expandableDescription.isVisible =
                    !expandableDescription.isVisible
            }
        }

    private fun updateHealthDetails(sectionDataList: SectionDataList) =
        with(binding.detailsExpandableHealthDetails) {
            expandableTitle.text = getString(sectionDataList.first.stringId)
            expandableDescription.text =
                sectionDataList.second.fromPresentationDataListToString(requireContext())
            expandableButton.onClickAnimateWithAction {
                expandableDescription.isVisible =
                    !expandableDescription.isVisible
            }
        }

    private fun updateDetails(sectionDataList: SectionDataList) =
        with(binding.detailsExpandableDetails) {
            expandableTitle.text = getString(sectionDataList.first.stringId)
            expandableDescription.text =
                sectionDataList.second.fromPresentationDataListToString(requireContext())
            expandableButton.onClickAnimateWithAction {
                expandableDescription.isVisible =
                    !expandableDescription.isVisible
            }
        }

    private fun updateDescription(sectionData: SectionData) =
        with(binding.detailsExpandableDescription) {
            expandableTitle.text = getString(sectionData.first.stringId)
            expandableDescription.text =
                sectionData.second.fromPresentationDataToString(requireContext())
            expandableButton.onClickAnimateWithAction {
                expandableDescription.isVisible =
                    !expandableDescription.isVisible
            }
        }

    private fun updateDetailsTop(uiState: DetailsUiState) = with(uiState.detailsHeadline) {
        binding.detailsImage.setImage(image, ScaleType.FIT_CENTER)
        binding.detailsImage.setOnClickListener {
            viewModel.navigateToGallery(GallerySender.GalleryArg.NoArg)
        }
        binding.detailsTitle.text = getString(R.string.separated_by_comma_strings, name, age)
        binding.detailsPublished.text = publishedAt
    }

    private fun setupArgs() = with(viewModel) {
        val id = animalDetailsNavigation.getNavArgs(this@AnimalDetailsFragment)
        setupArgs(idArgs = id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeGalleryReceiverJob?.cancel()
        observeUiStateJob?.cancel()
        subscriptions.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}