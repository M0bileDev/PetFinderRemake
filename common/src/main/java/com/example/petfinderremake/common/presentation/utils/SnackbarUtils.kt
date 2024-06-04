package com.example.petfinderremake.common.presentation.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showNotYetImplementedSnackBar(view: View, additionalMessage: String = "") = with(view) {
    Snackbar.make(
        this,
        this.resources.getString(commonString.feature_not_created_yet, additionalMessage),
        Snackbar.LENGTH_LONG
    ).show()
}

fun showPermissionNotGranted(permission:String, view: View) = with(view) {
    Snackbar.make(
        this,
        this.resources.getString(commonString.permission_not_granted, permission),
        Snackbar.LENGTH_LONG
    ).show()
}

fun showPermissionNotificationDenied(view: View) = with(view) {
    Snackbar.make(
        this,
        this.resources.getString(commonString.permission_notification_denied),
        Snackbar.LENGTH_LONG
    ).show()
}

fun showPermissionNotificationGranted(view: View) = with(view) {
    Snackbar.make(
        this,
        this.resources.getString(commonString.permission_notification_granted),
        Snackbar.LENGTH_LONG
    ).show()
}