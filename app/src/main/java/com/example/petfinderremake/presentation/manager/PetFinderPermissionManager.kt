package com.example.petfinderremake.presentation.manager

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.common.presentation.manager.permission.PermissionManager
import com.example.petfinderremake.common.presentation.manager.permission.PermissionSender
import com.example.petfinderremake.common.presentation.manager.permission.PermissionType
import com.example.petfinderremake.common.presentation.manager.permission.getPermissionName
import com.example.petfinderremake.common.presentation.utils.commonString
import com.example.petfinderremake.common.presentation.utils.showPermissionDialog
import com.example.petfinderremake.common.presentation.utils.showPermissionNotGranted
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Job
import javax.inject.Inject

class PetFinderPermissionManager @Inject constructor() : PermissionManager {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var context: Context
    private lateinit var view: View
    private var actionGranted: () -> Unit = {}
    private var actionDenied: () -> Unit = {}
    private var actionPermanentlyDenied: () -> Unit = {}

    // TODO: refactor to permission-rationale displayed pair [Permission to RationaleDisplayed]
    private var shouldShowRationaleDisplayed = false

//    override fun activitySetup(
//        activitySetup: FragmentActivity,
//        contextSetup: Context,
//        viewSetup: View
//    ) {
//        context = contextSetup
//        view = viewSetup
//
//        setupPermissionLauncher(activitySetup)
//    }

    override fun fragmentSetup(
        contextSetup: Context,
        viewSetup: View
    ) {
        context = contextSetup
        view = viewSetup
    }

//    private fun setupPermissionLauncher(fragmentActivity: FragmentActivity) {
//        with(fragmentActivity) {
//            permissionLauncher = registerForActivityResult(
//                ActivityResultContracts.RequestPermission(),
//            ) { isGranted ->
//                if (isGranted) {
//                    actionGranted()
//                } else {
//                    actionDenied()
//                }
//            }
//        }
//    }

    override fun fragmentBeforeSetup(
        fragmentSetup: Fragment
    ) {
        with(fragmentSetup) {
            permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission(),
            ) { isGranted ->
                when {
                    !isGranted && shouldShowRationaleDisplayed -> actionPermanentlyDenied()
                    !isGranted -> actionDenied()
                    else -> {
                        shouldShowRationaleDisplayed = false
                        actionGranted()
                    }
                }
            }
        }
    }

    override fun setupActions(
        actionGrantedSetup: () -> Unit,
        actionDeniedSetup: () -> Unit,
        actionPermanentlyDeniedSetup: () -> Unit,
    ) {
        actionGranted = actionGrantedSetup
        actionDenied = actionDeniedSetup
        actionPermanentlyDenied = actionPermanentlyDeniedSetup
    }

    override fun setupPermissionsReceiver(
        permissionSender: () -> PermissionSender,
        resources: Resources,
        lifecycleOwner: LifecycleOwner,
        jobBlock: (Job) -> Unit,
        onDispose: (Disposable) -> Unit
    ) = with(lifecycleOwner) {
        withLifecycleOwner(
            disposableBlock = {
                permissionSender().getPermissionEvent()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { event ->
                        when (event) {
                            is PermissionSender.PermissionEvent.ShowPermissionRationale -> {
                                showPermissionDialog(
                                    context = context,
                                    permissionMessage = commonString.permission_notification,
                                    onPositiveButton = {
                                        permissionLauncher.launch(event.permission)
                                    },
                                    onNegativeButton = {
                                        showPermissionNotGranted(
                                            getPermissionName(
                                                resources,
                                                event.permission
                                            ), view
                                        )
                                    })
                            }
                        }
                    }
            },
            jobBlock = jobBlock,
            onDispose = onDispose
        )
    }

    override fun checkPermissions(
        permissionTypes: List<PermissionType>,
        permissionsGranted: () -> Unit,
        permissionsDenied: () -> Unit
    ) {
        val permissionState = createPermissionState(permissionTypes)
        val notGrantedPermissions = permissionState.filter { !it.second }.map { it.first }
        if (notGrantedPermissions.isNotEmpty()) {
            permissionsDenied()
        } else {
            shouldShowRationaleDisplayed = false
            permissionsGranted()
        }
    }

    private fun createPermissionState(permissionTypes: List<PermissionType>): List<Pair<String, Boolean>> {
        val permissions = permissionTypes.map { it.manifestPermission.map { it } }.flatten()
        return permissions.map {
            it to (ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }


    override fun runPermissions(
        activity: Activity,
        permissionTypes: List<PermissionType>,
        runPermissionRationale: (String) -> Unit
    ) {
        val permissionState = createPermissionState(permissionTypes)
        val notGrantedPermissions = permissionState.filter { !it.second }.map { it.first }
        notGrantedPermissions.forEach { permission ->
            when {
                shouldShowRequestPermissionRationale(activity, permission) -> {
                    shouldShowRationaleDisplayed = true
                    runPermissionRationale(permission)
                }

                else -> {
                    permissionLauncher.launch(permission)
                }
            }
        }
    }
}