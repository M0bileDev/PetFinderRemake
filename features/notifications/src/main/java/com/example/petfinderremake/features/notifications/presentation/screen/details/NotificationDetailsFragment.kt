package com.example.petfinderremake.features.notifications.presentation.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.navigation.CommonNavigation
import com.example.petfinderremake.features.notifications.databinding.FragmentNotificationDetailsBinding
import com.example.petfinderremake.features.notifications.presentation.navigation.NotificationNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDetailsFragment : Fragment() {

    @Inject
    lateinit var notificationNavigation: NotificationNavigation

    @Inject
    lateinit var commonNavigation: CommonNavigation

    private var _binding: FragmentNotificationDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationDetailsViewModel by viewModels()

    private var observeUiStateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavArgs()
        setupToolbar()
        observeUiState()
    }

    private fun setupToolbar() = with(binding) {
        notificationDetailsToolbar.setNavigationOnClickListener {
            commonNavigation.navigateBack(this@NotificationDetailsFragment)
        }
    }

    private fun observeUiState() = with(viewModel) {
        observeUiStateJob = withLifecycleOwner {
            notificationDetailsUiState.collectLatest { uiState ->
                updateView(uiState)
            }
        }

    }

    private fun updateView(uiState: NotificationDetailsUiState) = with(uiState) {
        binding.notificationDetailsCreatedAt.text = createdAt
        binding.notificationDetailsTitle.text = title
        binding.notificationDetailsDescription.text = description
    }

    private fun setupNavArgs() = with(viewModel) {
        val notificationId =
            notificationNavigation.getNotificationIdNavArg(this@NotificationDetailsFragment)
        setupNavArgs(notificationId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeUiStateJob?.cancel()
        _binding = null
    }
}