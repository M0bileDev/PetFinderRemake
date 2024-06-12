package com.example.petfinderremake.common.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import com.example.petfinderremake.common.data.preferences.PreferencesConstants


fun Context.rxDataStore(): RxDataStore<Preferences> {
    return RxPreferenceDataStoreBuilder(this, PreferencesConstants.PETFINDER_DATASTORE_NAME).build()
}

fun Context.openWebPage(url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    startActivity(intent)
}

fun Context.openNotificationSettings(channelId: String? = null) {
    val notificationSettingsIntent =
//        when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O /*26*/ ->
        Intent().apply {
            action = when (channelId) {
                null -> Settings.ACTION_APP_NOTIFICATION_SETTINGS
                else -> Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
            }
            channelId?.let { putExtra(Settings.EXTRA_CHANNEL_ID, it) }
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /*28*/) {
                flags += Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*21*/ -> Intent().apply {
//            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
//            putExtra("app_package", packageName)
//            putExtra("app_uid", applicationInfo.uid)
//        }
//
//        Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT /*19*/ -> Intent().apply {
//            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            addCategory(Intent.CATEGORY_DEFAULT)
//            data = Uri.parse("package:${packageName}")
//        }
//
//        else -> null
//    }
    notificationSettingsIntent.let(::startActivity)
}