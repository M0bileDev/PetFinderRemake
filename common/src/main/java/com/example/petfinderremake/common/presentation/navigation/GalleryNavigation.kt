package com.example.petfinderremake.common.presentation.navigation

import androidx.fragment.app.Fragment
import com.example.petfinderremake.common.domain.model.animal.media.Media
import com.example.petfinderremake.common.presentation.navigation.model.GalleryNavArg

interface GalleryNavigation {
    fun navigateToGallery(fragment: Fragment, media: Media)
    fun getGalleryNavArg(fragment: Fragment): GalleryNavArg
}