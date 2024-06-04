package com.example.petfinderremake.common.presentation.navigation

import androidx.fragment.app.Fragment

interface AnimalDetailsNavigation {
    fun getNavArgs(fragment: Fragment): Long
    fun navigateToAnimalDetails(fragment: Fragment, id: Long)
}