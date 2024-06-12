package com.example.petfinderremake.features.notifications.presentation.screen.notification

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petfinderremake.common.ext.openNotificationSettings
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.manager.notification.NotificationManager
import com.example.petfinderremake.common.presentation.manager.permission.PermissionManager
import com.example.petfinderremake.common.presentation.manager.permission.PermissionType
import com.example.petfinderremake.common.presentation.utils.showNotYetImplementedSnackBar
import com.example.petfinderremake.features.notifications.databinding.FragmentNotificationBinding
import com.example.petfinderremake.features.notifications.presentation.adapter.NotificationAdapter
import com.example.petfinderremake.features.notifications.presentation.navigation.NotificationNavigation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    @Inject
    lateinit var notificationNavigation: NotificationNavigation

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var notificationManager: NotificationManager

    private val checkBuildPostNotifications =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    @SuppressLint("NewApi")
    private val permissions = listOf(PermissionType.NOTIFICATION)

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationViewModel by activityViewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    private var observeUiStateJob: Job? = null
    private var observePermissionReceiverJob: Job? = null
    private var observeEventJob: Job? = null

    private val subscriptions = CompositeDisposable()

    override fun onResume() {
        super.onResume()
        runIfPermissionRequired {
            with(viewModel) {
                permissionManager.checkPermissions(
                    permissionTypes = permissions,
                    permissionsGranted = {
                        notificationManager.registerPetFinderGeneralNotificationsChannel()
                        updateActionGranted(true)
                        updateNotificationsPermanentlyDenied(false)
                    },
                    permissionsDenied = {
                        notificationManager.unregisterPetFinderGeneralNotificationsChannel()
                        updateActionGranted(false)
                    })

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runIfPermissionRequired {
            permissionManager.fragmentBeforeSetup(this@NotificationFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root
        val context = view.context ?: throw Exception()

        runIfPermissionRequired {
            setupPermissionManager(view, context)
        }

        return view
    }

    private fun setupPermissionManager(view: View, context: Context) = with(viewModel) {
        permissionManager.apply {
            fragmentSetup(context, view)
            setupPermissionsReceiver(
                permissionSender = { this@with },
                resources = resources,
                lifecycleOwner = this@NotificationFragment.viewLifecycleOwner,
                jobBlock = {
                    observePermissionReceiverJob = it
                },
                onDispose = {
                    it.addTo(subscriptions)
                }
            )
            setupActions(
                actionGrantedSetup = {
                    updateActionGranted(true)
                },
                actionDeniedSetup = {
                    updateActionGranted(false)
                },
                actionPermanentlyDeniedSetup = {
                    updateNotificationsPermanentlyDenied(true)
                }
            )
            runPermissions(
                activity = this@NotificationFragment.requireActivity(),
                permissionTypes = permissions,
                runPermissionRationale = { permission ->
                    runPermissionRationale(permission)
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        runIfPermissionRequired {
            setupNotificationDisabledButton()
            setupNotificationPermanentlyDeniedButton()
        }
        observeUiState()
        observeNotificationEvent()
    }

    private fun observeNotificationEvent() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeEventJob = it
            },
            disposableBlock = {
                notificationEvent
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { event ->
                        when (event) {
                            is NotificationViewModel.NotificationEvent.NavigateToNotificationDetails -> {
                                notificationNavigation.navigateToNotificationDetails(
                                    this@NotificationFragment,
                                    event.notificationId
                                )
                            }
                        }
                    }.addTo(subscriptions)
            }
        )
    }

    private fun setupRecyclerView() = with(viewModel) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        notificationAdapter = NotificationAdapter(
            onClick = { notificationId ->
                navigateToNotificationDetails(notificationId)
            },
            onMoreClick = {
                showNotYetImplementedSnackBar(requireView())
            }
        )
        binding.notificationRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = notificationAdapter
        }
    }

    private fun setupNotificationPermanentlyDeniedButton() = with(binding) {
        notificationPermanentlyDenied.notificationDeniedButton.setOnClickListener {
            requireContext().openNotificationSettings()
        }
    }

    private fun setupNotificationDisabledButton() = with(binding) {
        notificationDisabled.notificationDisabledButton.setOnClickListener {
            permissionManager.runPermissions(
                activity = this@NotificationFragment.requireActivity(),
                permissionTypes = permissions,
                runPermissionRationale = { permission ->
                    viewModel.runPermissionRationale(permission)
                }
            )
        }
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner(
            jobBlock = {
                observeUiStateJob = it
            },
            disposableBlock = {
                notificationUiState
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { uiState ->
                        updateView(uiState)
                        updateRecyclerView(uiState)
                    }.addTo(subscriptions)
            }
        )
    }

    private fun updateRecyclerView(uiState: NotificationUiState) = with(uiState) {
        notificationAdapter.submitList(notifications)
    }

    private fun updateView(uiState: NotificationUiState) = with(binding) {
        notificationRecyclerView.isVisible = uiState.granted
        notificationNestedScroll.isVisible = uiState.notGranted
        notificationDisabled.root.isVisible = uiState.denied
        notificationPermanentlyDenied.root.isVisible = uiState.permanentlyDenied
    }

    private fun runIfPermissionRequired(block: () -> Unit) {
        if (checkBuildPostNotifications) {
            block()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        observeUiStateJob?.cancel()
        observePermissionReceiverJob?.cancel()
        observeEventJob?.cancel()
        subscriptions.clear()
        binding.notificationRecyclerView.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}