package com.example.petfinderremake.presentation.navigation

import androidx.navigation.NavOptions
import com.example.petfinderremake.R

interface DefaultNavOptions {

    fun getBottomNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setPopUpTo(R.id.discoverFragment, inclusive = false, saveState = true)
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .build()
    }
}