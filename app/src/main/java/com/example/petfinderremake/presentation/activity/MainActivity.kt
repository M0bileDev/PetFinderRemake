package com.example.petfinderremake.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petfinderremake.R
import com.example.petfinderremake.common.ext.withLifecycleOwner
import com.example.petfinderremake.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var badge: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setupBottomNavigation()
        setupBottomNavigationBadge()
        observeUiState()
    }

    private fun setupBottomNavigationBadge() {
        badge = binding.bottomNavigation.getOrCreateBadge(R.id.notificationFragment)
    }

    private fun observeUiState() = with(viewModel) {
        withLifecycleOwner {
            mainActivityUiState.collectLatest { uiState ->
                updateFilterMenuBadge(uiState)
            }
        }
    }

    private fun updateFilterMenuBadge(uiState: MainActivityUiState) = with(uiState) {
        if (showNotificationsNotDisplayed) {
            badge.backgroundColor =
                resources.getColor(androidx.appcompat.R.color.error_color_material_dark, null)
        } else {
            badge.backgroundColor =
                resources.getColor(
                    android.R.color.transparent,
                    null
                )
        }
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isGone = when (destination.id) {
                R.id.filterFragment, R.id.selectFragment, R.id.galleryFragment, R.id.animalDetailsFragment -> true
                else -> false
            }
        }
        binding.bottomNavigation.setupWithNavController(navController)
    }
}