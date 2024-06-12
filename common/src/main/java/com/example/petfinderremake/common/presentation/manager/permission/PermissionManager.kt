package com.example.petfinderremake.common.presentation.manager.permission

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Job

interface PermissionManager {


//    fun activitySetup(
//        activitySetup: FragmentActivity,
//        contextSetup: Context,
//        viewSetup: View
//    )

    fun fragmentSetup(
        contextSetup: Context,
        viewSetup: View
    )

    fun fragmentBeforeSetup(fragmentSetup: Fragment)

    fun setupActions(
        actionGrantedSetup: () -> Unit = {},
        actionDeniedSetup: () -> Unit = {},
        actionPermanentlyDeniedSetup: () -> Unit,
    )

    fun checkPermissions(
        permissionTypes: List<PermissionType>,
        permissionsGranted: () -> Unit,
        permissionsDenied: () -> Unit
    )

    fun setupPermissionsReceiver(
        permissionSender: () -> PermissionSender,
        resources: Resources,
        lifecycleOwner: LifecycleOwner,
        jobBlock: (Job) -> Unit,
        onDispose: (Disposable) -> Unit
    )

    fun runPermissions(
        activity: Activity,
        permissionTypes: List<PermissionType>,
        runPermissionRationale: (String) -> Unit
    )
}



