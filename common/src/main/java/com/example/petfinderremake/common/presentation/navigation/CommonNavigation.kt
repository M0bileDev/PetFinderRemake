package com.example.petfinderremake.common.presentation.navigation

import androidx.fragment.app.Fragment

interface CommonNavigation {
    fun navigateBack(fragment: Fragment): Boolean
}