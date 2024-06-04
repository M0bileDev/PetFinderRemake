package com.example.petfinderremake.features.discover.presentation.navigation

import androidx.fragment.app.Fragment

interface DiscoverNavigation {
    fun navigateToSearch(fragment: Fragment, typeName: String)
}