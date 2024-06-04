package com.example.petfinderremake.common.presentation.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.petfinderremake.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showErrorDialog(
    context: Context,
    errorTitle: Int,
    errorMessage: Int,
    supportMessage: Int = commonString.error_contact_support,
    onPositiveButton: () -> Unit = {},
    onNegativeButton: () -> Unit = {}
) : AlertDialog = with(context) {
    MaterialAlertDialogBuilder(this)
        .setTitle(errorTitle)
        .setIcon(R.drawable.ic_error)
        .setMessage(
            resources.getString(errorMessage) + resources.getString(
                supportMessage
            )
        )
        .setNegativeButton(commonString.cancel) { dialog, _ ->
            dialog.dismiss()
            onNegativeButton()
        }
        .setPositiveButton(commonString.support) { dialog, _ ->
            dialog.dismiss()
            onPositiveButton()
        }.show()
}

fun showPermissionDialog(
    context: Context,
    permissionTitle: Int = commonString.permission_title,
    permissionMessage: Int,
    onPositiveButton: () -> Unit = {},
    onNegativeButton: () -> Unit = {}
) : AlertDialog = with(context) {
    return@with MaterialAlertDialogBuilder(this)
        .setTitle(permissionTitle)
        .setIcon(R.drawable.ic_error)
        .setMessage(
            resources.getString(permissionMessage)
        )
        .setNegativeButton(commonString.permission_no_thanks) { dialog, _ ->
            dialog.dismiss()
            onNegativeButton()
        }
        .setPositiveButton(commonString.permission_ok) { dialog, _ ->
            dialog.dismiss()
            onPositiveButton()
        }.show()
}