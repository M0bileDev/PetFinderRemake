package com.example.petfinderremake.common.presentation.manager.permission

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.petfinderremake.common.presentation.utils.commonString

object PermissionUtils {

}

enum class PermissionType(vararg val manifestPermission: String) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION(Manifest.permission.POST_NOTIFICATIONS)
}

fun getPermissionName(resources: Resources, permission: String) =
    when (permission) {
        Manifest.permission.POST_NOTIFICATIONS -> resources.getString(commonString.notifications)
        else -> throw Exception()
    }